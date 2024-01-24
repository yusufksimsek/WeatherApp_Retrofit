package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import model.WeatherModel
import service.WeatherAPIService

class MainViewModel:ViewModel() {

    private val weatherAPIService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_load = MutableLiveData<Boolean>()

    fun refreshData(cityName: String){
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {
        weather_load.value = true
        disposable.add(
            weatherAPIService.getDataService(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                    weather_data.value = t
                    weather_error.value = false
                    weather_error.value = false
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value = true
                        weather_load.value = false
                    }

                })
        )
    }


}