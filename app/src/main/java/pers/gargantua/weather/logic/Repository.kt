package pers.gargantua.weather.logic

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import pers.gargantua.weather.logic.dao.DataDao
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Weather
import pers.gargantua.weather.logic.network.Network
import kotlin.Exception
import kotlin.coroutines.CoroutineContext

/**
 * @author Gargantua丶
 **/
object Repository {

    fun save() = DataDao.save()

    fun getSave() = DataDao.getSave()

    fun isSaved() = DataDao.isSaved()

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

    fun getWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        val weatherResponse = Network.getWeather(lng, lat)
        if (weatherResponse.status == "ok") {
            Result.success(weatherResponse)
        } else {
            Result.failure(ResponseStatusException("weather response status is ${weatherResponse.status}"))
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
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