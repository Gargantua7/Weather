package pers.gargantua.weather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @author Gargantua丶
 **/
/**
 * 用于存储彩云 API 地点搜索中的数据模型类
 */
data class PlaceRepo(
    /** 表示请求返回体状态，请求正常返回 'ok' */
    val status: String,
    /** 返回体中地点的数据集合 */
    val places: List<Place>
)

/**
 * 用于数据持久化时据传递的数据模型类
 */
data class DaoPlace(
    /** 地点 ID ，通常用于识别地点是否已添加 */
    val id: String,
    /** 地点名 */
    var name: String,
    /** 地点坐标，用于请求天气数据 */
    var location: Location,
    /** 地点天气，可空，在本类中用于在二次启动 APP 后请求新数据前能展示旧数据而不是空白 */
    var weather: Weather? = null
)

/**
 * 彩云 API 地点查询返回体中单个地点的数据模型
 */
data class Place(
    /** 地点 ID ，通常用于识别地点是否已添加 */
    val id: String,
    /** 地点名 */
    val name: String,
    /** 地点坐标，用于请求天气数据 */
    val location: Location,
    /** 地点描述*/
    @SerializedName("formatted_address")
    val address: String,
    /** 地点天气，彩云地点查询 API 返回体中不包含天气信息，即需要单独查询 */
    var weather: Weather? = null
)

/**
 * 百度地图 SDK 获取定位后用于传递数据的数据模型类
 */
data class BDPlace(
    /** 地点名 */
    val name: String,
    /** 地点坐标，用于请求天气数据 */
    val location: Location,
    /**
     * 请求状态，请求成功返回数值的为 '161'
     * @see [百度地图SDK定位错误返回码]
     * (http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code)
     */
    val status: Int
)

/**
 * 位置坐标信息数据模型类
 */
data class Location(
    /** 经度 */
    val lng: String,
    /** 纬度 */
    val lat: String
)
