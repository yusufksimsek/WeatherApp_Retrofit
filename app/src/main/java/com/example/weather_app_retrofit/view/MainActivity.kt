package com.example.weather_app_retrofit.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather_app_retrofit.R
import com.example.weather_app_retrofit.databinding.ActivityMainBinding
import retrofit2.http.GET
import viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: MainViewModel
    private lateinit var GET:SharedPreferences
    private lateinit var SET:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProvider(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName","izmir")
        binding.editCityName.setText(cName)

        viewmodel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.llDataView.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE

            var cityName = GET.getString("cityName",cName)
            binding.editCityName.setText(cityName)
            viewmodel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearchCityName.setOnClickListener {
            val cityName = binding.editCityName.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()

        }

    }

    private fun getLiveData() {
        viewmodel.weather_data.observe(this, Observer { data ->
            data.let {
                binding.llDataView.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
                binding.tvDegree.text = "${data.main.temp.toString()} °C"
                binding.tvCountryCode.text = data.sys.country.toString()
                binding.tvCityName.text = data.name.toString()
                binding.tvSpeed.text = "%${data.wind.speed.toString()}"
                binding.tvHumidity.text = data.main.humidity.toString()
                binding.tvFeelsLike.text = data.main.feels_like.toString()
                binding.tvPressure.text = data.main.pressure.toString()

                Glide.with(this).load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(binding.imgWeatherIcon)

            }
        })

        viewmodel.weather_load.observe(this, Observer { load ->
            load.let {
                if(it){
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }else{
                    binding.pbLoading.visibility = View.GONE
                }
            }
        })

        viewmodel.weather_error.observe(this, Observer { error ->
            error.let {
                if (it){
                    binding.tvError.visibility = View.VISIBLE
                    binding.llDataView.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE
                }else{
                    binding.tvError.visibility = View.GONE
                }
            }
        })

    }
}

//https://api.openweathermap.org/data/2.5/weather?q=izmir&appid=f0c25276d744c72c92bc40d0170c07cb
//https://api.openweathermap.org/data/2.5/weather?q=izmir&appid=f0c25276d744c72c92bc40d0170c07cb