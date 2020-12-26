package pers.gargantua.weather.logic.network

import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.model.PlaceRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 彩云天气 API 地点查询接口
 * @author Gargantua丶
 **/
interface PlaceService {
    @GET("v2/place?token=${WeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query") query: String) : Call<PlaceRepo>
}