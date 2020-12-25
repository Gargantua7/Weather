package pers.gargantua.weather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log

/**
 * @author Gargantua丶
 **/
@SuppressLint("Registered")
class WeatherApplicationPrivate : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "" /* 彩云天气api TOKEN*/

        fun log(obj: Class<Any>, key: String) {
            Log.d("gargantua-log", "${obj.name}: $key")
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}