package pers.gargantua.weather.logic

import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Sky
import kotlin.math.roundToInt

/**
 * 仓库层 - 全局数据/方法类
 * @author Gargantua丶
 **/
object GlobalData {

    /** 全局地点天气数据 */
    val places = ArrayList<DaoPlace>()

    /** 获取当日是周几的字符串资源，起始为周日 */
    val weekday = listOf(
        R.string.sun,
        R.string.mon,
        R.string.tue,
        R.string.wed,
        R.string.thu,
        R.string.fri,
        R.string.sat
    )

    /** 风向字符串资源 */
    private val windDirection = listOf(
        R.string.wind_north,
        R.string.wind_north_east,
        R.string.wind_east,
        R.string.wind_south_east,
        R.string.wind_south,
        R.string.wind_south_west,
        R.string.wind_west,
        R.string.wind_west_north
    )

    /** 根据风向角度直接获取字符串资源 */
    fun getWindDirection(direction: Double): Int {
        return windDirection[(direction.roundToInt() + 20) / 40 % 8]
    }

    /** 紫外线强度字符串资源，为配合 API 返回体，起始下标为 1 */
    val ultraviolet = listOf(
        0,
        R.string.daily_ultraviolet1,
        R.string.daily_ultraviolet2,
        R.string.daily_ultraviolet3,
        R.string.daily_ultraviolet4,
        R.string.daily_ultraviolet5
    )

    /** 穿衣指数字符串资源 */
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

    /** 易感程度字符串资源，为配合 API 返回体，起始下标为 1 */
    val coldRisk = listOf(
        0,
        R.string.coldRisk1,
        R.string.coldRisk2,
        R.string.coldRisk3,
        R.string.coldRisk4
    )

    /** 洗车指数字符串资源，为配合 API 返回体，起始下标为 1 */
    val carWashing = listOf(
        0,
        R.string.carWashing1,
        R.string.carWashing2,
        R.string.carWashing3,
        R.string.carWashing4
    )

    /**
     * 天相静态数据
     * @see Sky
     */
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

    /**
     * 将风速 (m/s) 转换为蒲福风力等级
     */
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

    /** 空气指数简短描述 */
    const val AQI_SHORT_DESCRIPTION = 0
    /** 空气指数详细描述 */
    const val AQI_LONG_DESCRIPTION = 1
    /**
     * 将空气指数转为语言描述的字符串
     * @param mode 返回描述的详细度，值为 [AQI_SHORT_DESCRIPTION] 或 [AQI_LONG_DESCRIPTION] (默认)
     */
    fun Double.toAqi(mode: Int = AQI_LONG_DESCRIPTION): String =
        WeatherApplication.context.run {
            getString(
                when (roundToInt()) {
                    in 0..50 -> R.string.aqi_great
                    in 50..100 -> R.string.aqi_good
                    in 100..150 -> R.string.aqi_light
                    in 150..200 -> R.string.aqi_moderate
                    else -> R.string.aqi_severe
                }
            ) + if (mode == AQI_LONG_DESCRIPTION)
                when (roundToInt()) {
                    in 0..100 -> ""
                    else -> getString(R.string.aqi_pollution)
                }
            else ""
        }
}