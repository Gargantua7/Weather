package pers.gargantua.weather.logic

import pers.gargantua.weather.R
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Sky
import kotlin.math.roundToInt

/**
 * @author Gargantua丶
 **/
object GlobalData {

    val places = ArrayList<DaoPlace>()

    val weekday = listOf(
        R.string.sun,
        R.string.mon,
        R.string.tue,
        R.string.wed,
        R.string.thu,
        R.string.fri,
        R.string.sat
    )

    val windDirection = listOf(
        R.string.wind_north,
        R.string.wind_north_east,
        R.string.wind_east,
        R.string.wind_south_east,
        R.string.wind_south,
        R.string.wind_south_west,
        R.string.wind_west,
        R.string.wind_west_north
    )

    val ultraviolet = listOf(
        R.string.daily_ultraviolet1,
        R.string.daily_ultraviolet2,
        R.string.daily_ultraviolet3,
        R.string.daily_ultraviolet4,
        R.string.daily_ultraviolet5
    )

    val dressing = listOf(
        R.string.dressing0,
        R.string.dressing1,
        R.string.dressing2,
        R.string.dressing3,
        R.string.dressing4,
        R.string.dressing5,
        R.string.dressing6,
        R.string.dressing7,
        R.string.dressing8
    )

    val coldRisk = listOf(
        R.string.coldRisk1,
        R.string.coldRisk2,
        R.string.coldRisk3,
        R.string.coldRisk4
    )

    val carWashing = listOf(
        R.string.carWashing1,
        R.string.carWashing2,
        R.string.carWashing3,
        R.string.carWashing4
    )

    val sky = mapOf(
        "CLEAR_DAY" to Sky("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day, R.color.clear_day),
        "CLEAR_NIGHT" to Sky("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night, R.color.clear_night),
        "PARTLY_CLOUDY_DAY" to Sky(
            "多云",
            R.drawable.ic_partly_cloud_day,
            R.drawable.bg_partly_cloudy_day,
            R.color.partly_cloudy_day
        ),
        "PARTLY_CLOUDY_NIGHT" to Sky(
            "多云",
            R.drawable.ic_partly_cloud_night,
            R.drawable.bg_partly_cloudy_night,
            R.color.partly_cloudy_night
        ),
        "CLOUDY" to Sky("阴", R.drawable.ic_cloudy, R.drawable.bg_cloudy, R.color.cloudy),
        "WIND" to Sky("大风", R.drawable.ic_cloudy, R.drawable.bg_wind, R.color.wind),
        "LIGHT_RAIN" to Sky("小雨", R.drawable.ic_light_rain, R.drawable.bg_rain, R.color.rain),
        "MODERATE_RAIN" to Sky("中雨", R.drawable.ic_moderate_rain, R.drawable.bg_rain, R.color.rain),
        "HEAVY_RAIN" to Sky("大雨", R.drawable.ic_heavy_rain, R.drawable.bg_rain, R.color.rain),
        "STORM_RAIN" to Sky("暴雨", R.drawable.ic_storm_rain, R.drawable.bg_rain, R.color.rain),
        "THUNDER_SHOWER" to Sky("雷阵雨", R.drawable.ic_thunder_shower, R.drawable.bg_rain, R.color.rain),
        "SLEET" to Sky("雨夹雪", R.drawable.ic_sleet, R.drawable.bg_rain, R.color.rain),
        "LIGHT_SNOW" to Sky("小雪", R.drawable.ic_light_snow, R.drawable.bg_snow, R.color.snow),
        "MODERATE_SNOW" to Sky("中雪", R.drawable.ic_moderate_snow, R.drawable.bg_snow, R.color.snow),
        "HEAVY_SNOW" to Sky("大雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow, R.color.snow),
        "STORM_SNOW" to Sky("暴雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow, R.color.snow),
        "HAIL" to Sky("冰雹", R.drawable.ic_hail, R.drawable.bg_snow, R.color.snow),
        "LIGHT_HAZE" to Sky("轻度雾霾", R.drawable.ic_light_haze, R.drawable.bg_fog, R.color.fog),
        "MODERATE_HAZE" to Sky("中度雾霾", R.drawable.ic_moderate_haze, R.drawable.bg_fog, R.color.fog),
        "HEAVY_HAZE" to Sky("重度雾霾", R.drawable.ic_heavy_haze, R.drawable.bg_fog, R.color.fog),
        "FOG" to Sky("雾", R.drawable.ic_fog, R.drawable.bg_fog, R.color.fog),
        "DUST" to Sky("浮尘", R.drawable.ic_fog, R.drawable.bg_fog, R.color.fog)
    )

    fun Double.toBeaufortScale(): Int =
        when (roundToInt()) {
            0 -> 0
            in 1..5 -> 1
            in 6..11 -> 2
            in 12..19 -> 3
            in 20..28 -> 4
            in 29..38 -> 5
            in 39..49 -> 6
            in 50..61 -> 7
            in 62..74 -> 8
            in 75..88 -> 9
            in 89..102 -> 10
            in 103..117 -> 11
            in 118..133 -> 12
            in 134..149 -> 13
            in 150..166 -> 14
            in 167..183 -> 15
            in 184..201 -> 16
            else -> 17
        }

    fun Double.toAqi(): String =
        when (roundToInt()) {
            in 0..50 -> "优"
            in 50..100 -> "良"
            in 100..150 -> "轻度污染"
            in 150..200 -> "中度污染"
            else -> "重度污染"
        }

}