package pers.gargantua.weather.logic.model

import pers.gargantua.weather.logic.GlobalData

/**
 * @author Gargantua丶
 **/

data class Sky(val info: String, val icon: Int, val bg: Int, val color: Int) {
    companion object {
        operator fun get(skycon: String): Sky {
            return GlobalData.sky[skycon] ?: GlobalData.sky["CLEAR_DAY"]!!
        }
    }
}