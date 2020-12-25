package pers.gargantua.weather.logic.model

import pers.gargantua.weather.logic.GlobalData

/**
 * 用于根据彩云 API 返回体中的天相 [Weather.Skycon.value] 获取
 * 天气信息 [info]，图标 [icon]，背景 [bg]，主题色 [color]
 *
 * 获取信息可使用本类提供的操作方法 [get]
 *
 * @see GlobalData.sky
 * @author Gargantua丶
 **/
data class Sky(
    /** 彩云 API 返回的天气信息 */
    val info: String,
    /** 天气图标 */
    val icon: Int,
    /** 背景图片 */
    val bg: Int,
    /** 主题色 */
    val color: Int
) {
    companion object {
        /**
         * 获取包括所有信息的 [Sky] 对象
         * @param skycon 彩云 API 返回体中的天相 [Weather.Skycon.value]
         * @return 包含对应数据的 [Sky] 数据类对象
         */
        operator fun get(skycon: String): Sky {
            return GlobalData.sky[skycon] ?: (GlobalData.sky["CLEAR_DAY"] ?: error(""))
        }
    }
}