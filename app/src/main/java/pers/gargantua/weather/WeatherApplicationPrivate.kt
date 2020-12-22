package pers.gargantua.weather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author Gargantua丶
 **/
@SuppressLint("Registered")
class WeatherApplicationPrivate : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "" /* 彩云天气api TOKEN*/
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}