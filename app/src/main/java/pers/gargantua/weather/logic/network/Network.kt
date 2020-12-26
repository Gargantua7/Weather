package pers.gargantua.weather.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 网络层
 * @author Gargantua丶
 **/
object Network {

    /** 地点查询动态代理对象 */
    private val placeService = ServiceCreator.create<PlaceService>()

    /** 天气查询动态代理对象 */
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlace(query).await()

    suspend fun getWeather(lng: String, lat: String) = weatherService.getWeather(lng, lat).await()

    /**
     * 阻塞协程并且等待服务器响应，并返回给上一层
     */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) it.resume(body)
                    else it.resumeWithException(
                        RuntimeException("Response Body is NULL")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}