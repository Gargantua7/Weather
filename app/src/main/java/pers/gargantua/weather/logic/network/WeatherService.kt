package pers.gargantua.weather.logic.network

import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 彩云天气 API 天气查询接口
 * @author Gargantua丶
 **/
interface WeatherService {
    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/weather.json")
    fun getWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<Weather>
}