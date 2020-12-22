package pers.gargantua.weather.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.model.DaoPlace

/**
 * @author Gargantua丶
 **/
object DataDao {

    private fun sharedPreferences() =
        WeatherApplication.context.getSharedPreferences("weather", Context.MODE_PRIVATE)

    fun save() {
        sharedPreferences().edit {
            putString("places", Gson().toJson(GlobalData.places))
        }
    }

    fun getSave(): List<DaoPlace> {
        val placesJson = sharedPreferences().getString("places", "")
        val places = JsonParser().parse(placesJson).asJsonArray
        val gson = Gson()
        return ArrayList<DaoPlace>().apply {
            for(place in places) {
                add(gson.fromJson(place, DaoPlace::class.java))
            }
        }
    }

    fun isSaved() = sharedPreferences().contains("places")

}