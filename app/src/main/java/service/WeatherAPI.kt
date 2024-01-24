package service

import io.reactivex.Single
import model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=izmir&appid=f0c25276d744c72c92bc40d0170c07cb

interface WeatherAPI {
    @GET("data/2.5/weather?&units=metric&appid=f0c25276d744c72c92bc40d0170c07cb")
    fun getData(@Query("q") cityName: String): Single<WeatherModel>
}

