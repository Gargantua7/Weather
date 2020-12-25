package pers.gargantua.weather.logic.dao

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonParser
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.model.DaoPlace

/**
 * 管理数据持久化的类
 * @author Gargantua丶
 **/
object DataDao {

    private fun sharedPreferences() =
        WeatherApplication.context.getSharedPreferences("weather", Context.MODE_PRIVATE)

    /**
     * 将 [GlobalData.places] 转化为 JSON 字符串保存
     */
    fun save() {
        Log.d("gargantua-log-id", GlobalData.places.size.toString())
        sharedPreferences().edit {
            putString("places", Gson().toJson(GlobalData.places))
        }
    }

    /**
     * 提取 JSON 字符串并解析为 [DaoPlace] 数组保存至 [GlobalData.places]
     */
    fun getSave() {
        Log.d("gargantua-log-s", "get save")
        val placesJson = sharedPreferences().getString("places", "")
        val places = JsonParser().parse(placesJson).asJsonArray
        val gson = Gson()
        GlobalData.places.clear()
        GlobalData.places.apply {
            for (place in places) {
                add(gson.fromJson(place, DaoPlace::class.java))
            }
        }
    }

    /**
     * 用于检查本地是否已有保存了的数据
     */
    fun isSaved() = sharedPreferences().contains("places")

}