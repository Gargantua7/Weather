package pers.gargantua.weather.logic

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import pers.gargantua.weather.logic.dao.DataDao
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Place
import pers.gargantua.weather.logic.model.Weather
import pers.gargantua.weather.logic.network.Network
import kotlin.Exception
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层
 * @author Gargantua丶
 **/
object Repository {

    fun save() = DataDao.save()

    fun getSave() = DataDao.getSave()

    fun isSaved() = DataDao.isSaved()

    /**
     * 通过 Kotlin 协程异步拉取地点集合信息，并返回给 [Result]，
     * 之后在 [pers.gargantua.weather.ui.place.PlaceActivity] 中取出
     * @param query 查询地点的关键字
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = Network.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            for (place in places) {
                val lng = place.location.lng
                val lat = place.location.lat
                val weather = Network.getWeather(lng, lat)
                if (weather.status == "ok") {
                    place.weather = weather
                } else {
                    Result.failure<Weather>(ResponseStatusException("weather response status is ${weather.status}"))
                }
            }
            Result.success(places)
        } else {
            Result.failure(ResponseStatusException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 根据坐标通过 Kotlin 协程异步拉取天气信息，并返回给 [Result]
     * @param location 需要查询的地点的坐标
     */
    fun getWeather(location: Location) = fire(Dispatchers.IO) {
        val weather = Network.getWeather(location.lng, location.lat)
        if (weather.status == "ok") {
            Result.success(weather)
        } else {
            Result.failure(ResponseStatusException("weather response status is ${weather.status}"))
        }
    }

    /**
     * Lambda 高阶函数，目的是通过 livedata 以响应式编程的方式将异步获取的数据打包发送
     * 其中 lambda 已被 suspend 修饰为挂起函数
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        // <Result<T>> 删了会在 Gradle 编译阶段报错 KotlinFrontEndException
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Log.d("gargantua-log", e.toString())
                Result.failure<T>(e)
            }
            emit(result)
        }
}