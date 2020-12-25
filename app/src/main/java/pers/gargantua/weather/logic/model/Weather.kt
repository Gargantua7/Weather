package pers.gargantua.weather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 彩云 API 返回体天气信息数据类
 * @author Gargantua丶
 **/
data class Weather(
    /** 请求返回体状态，正常返回值为 'ok' */
    val status: String,
    /** 当前城市所在时区，用于在 UI 层小时级预报正确展示当地时间 */
    val timezone: String,
    /** 返回结果正文 */
    val result: Result
) {
    data class Result(
        /** 预测关键点 */
        @SerializedName("forecast_keypoint")
        val forecastKeypoint: String,
        /** 实时天气 */
        val realtime: Realtime,
        /** 小时级预报 */
        val hourly: Hourly,
        /** 未来三天预报 */
        val daily: Daily
    )

    /** 实时天气数据类 */
    data class Realtime(
        val status: String,
        /** 温度 - ℃ */
        val temperature: Double,
        /** 相对湿度 */
        val humidity: Double,
        /** 天气 */
        val skycon: String,
        /** 风 */
        val wind: Wind,
        /** 气压 - Pa */
        val pressure: Double,
        /** 体感温度 - ℃ */
        @SerializedName("apparent_temperature")
        val apparentTemperature: Double,
        /** 空气 */
        @SerializedName("air_quality")
        val airQuality: RealtimeAirQuality
    )

    /**
     * 未来 48 小时的天气数据类
     *
     * 建议使用本类提供的 [get] 操作方法可以直接获取某一时间的所有提供的天气数据
     */
    data class Hourly(
        val status: String,
        /** 温度 - ℃ */
        val temperature: List<HourTemp>,
        /** 风 */
        val wind: List<Wind>,
        /** 天气 */
        val skycon: List<Skycon>,
        /** 空气 */
        @SerializedName("air_quality")
        val airQuality: HourlyAirQuality
    ) {
        /**
         * 将未来第 time 小时天气数据打包为 Map，
         *
         * 通常彩云天气仅返回 48 小时的天气数据，即 index 取值范围应为 0 - 47
         *
         * 但本方法并未检查 index 是否合法，请注意 [java.lang.ArrayIndexOutOfBoundsException]
         *
         * ——————————————————————
         *
         * key - value - value type
         *
         * time - 当前本地时间 - [Date]
         *
         * temp - 温度 - [Double]
         *
         * windSpeed - 风速 - [Double]
         *
         * windDirection - 风向 - [Double]
         *
         * skycon - 天气情况 - [String]
         *
         * aqi - 空气指数 - [Double]
         *
         * @param time 所需未来天气的时间
         * @return [Map]<String, Any> 包含了未来第 time 小时的天气数据
         */
        operator fun get(time: Int) = mapOf<String, Any>(
            "time" to temperature[time].datetime,
            "temp" to temperature[time].value,
            "windSpeed" to wind[time].speed,
            "windDirection" to wind[time].direction,
            "skycon" to skycon[time].value,
            "aqi" to airQuality[time]
        )
    }

    /**
     * 未来 5 天的天气数据类
     *
     * 建议本类提供的 [get] 操作方法可以直接获取某一天的所有提供的天气数据
     */
    data class Daily(
        val status: String,
        /** 温度 - ℃ */
        val temperature: List<DailyTemp>,
        /** 空气 */
        @SerializedName("air_quality")
        val airQuality: DailyAirQuality,
        /** 天气 */
        val skycon: List<Skycon>,
        /** 生活指标 */
        @SerializedName("life_index")
        val life: Life
    ) {
        /**
         * 将未来第 time 天天气数据打包为 Map
         *
         * 通常彩云天气仅返回 5 天 (包括今天）的天气数据，即 index 取值范围应为 0 - 4
         *
         * 但本方法并未检查 index 是否合法，请注意 [java.lang.ArrayIndexOutOfBoundsException]
         *
         * ——————————————————————
         *
         * key - value - value type
         *
         * time - 当前本地时间 - [Date]
         *
         * maxTemp - 最高温度 - [Double]
         *
         * minTemp - 最低温度 - [Double]
         *
         * skycon - 天气情况 - [String]
         *
         * aqi - 空气指数 - [Double]
         *
         * —————————生活指数—————————
         *
         * Int 转 String 解码请参考 [pers.gargantua.weather.logic.GlobalData.toAqi]
         *
         * coldRisk - 易感程度 - [Int]
         *
         * dressing - 穿衣指数 - [Int]
         *
         * ultraviolet - 紫外线强度 - [Int]
         *
         * carWashing - 洗车指数 - [Int]
         *
         * @param time 所需未来天气的时间
         * @return [Map]<String, Any> 包含了未来第 time 天的天气数据
         */
        operator fun get(time: Int) = mapOf<String, Any>(
            "time" to temperature[time].date,
            "maxTemp" to temperature[time].max,
            "minTemp" to temperature[time].min,
            "aqi" to airQuality[time],
            "skycon" to skycon[time].value,
            "coldRisk" to life.coldRisk[time].index,
            "dressing" to life.dressing[time].index,
            "ultraviolet" to life.ultraviolet[time].index,
            "carWashing" to life.carWashing[time].index
        )
    }

    /** 风 */
    data class Wind(
        /** 风速 - m/s */
        val speed: Double,
        /** 风向 - 从北顺时针(0~360°) 获取风向字符串资源见 [pers.gargantua.weather.logic.GlobalData]*/
        val direction: Double
    )

    /** 实时空气质量数据 */
    data class RealtimeAirQuality(
        /** PM 2.5 */
        val pm25: Double,
        /** PM 10 */
        val pm10: Double,
        /** 臭氧 */
        val o3: Double,
        /** 二氧化硫 */
        val so2: Double,
        /** 三氧化氮 */
        val no3: Double,
        /** 一氧化碳 */
        val co: Double,
        /** 空气指数 */
        val aqi: AQI
    )

    /** 空气指数 */
    data class AQI(/** 国标数据 */val chn: Double, val date: Date)

    /** 生活指数数值 */
    data class LifeIndex(val index: Int)

    /** 生活指数 */
    data class Life(
        /** 紫外线强度 */
        val ultraviolet: List<LifeIndex>,
        /** 洗车指数 */
        val carWashing: List<LifeIndex>,
        /** 穿衣指数 */
        val dressing: List<LifeIndex>,
        /** 易感程度 */
        val coldRisk: List<LifeIndex>
    )

    /** 小时级预报温度数据 */
    data class HourTemp(val value: Double, val datetime: Date)

    /** 天级预报温度数据 */
    data class DailyTemp(val max: Double, val min: Double, val date: Date)


    /** 天相数据 */
    data class Skycon(val value: String, val datetime: Date)

    /** 小时级预报的空气指数 */
    data class HourlyAQI(val value: AQI, val datetime: Date)

    /** 小时级预报的空气指数列表 */
    data class HourlyAirQuality(val aqi: List<HourlyAQI>) {
        operator fun get(index: Int) = aqi[index].value.chn
    }

    /** 天级预报的空气指数 */
    data class DailyAQI(val avg: AQI, val date: Date)

    /** 天级预报的空气指数列表 */
    data class DailyAirQuality(val aqi: List<DailyAQI>) {
        operator fun get(index: Int) = aqi[index].avg.chn
    }
}