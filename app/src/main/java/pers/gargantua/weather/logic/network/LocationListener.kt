package pers.gargantua.weather.logic.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.model.BDPlace
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location

/**
 * 百度地图 SDK 回调类
 * @author Gargantua丶
 **/
class LocationListener : BDAbstractLocationListener() {

    /** LiveData 对象，用于监听是否回调 */
    val place = MutableLiveData<BDPlace>()

    /**
     * 百度地图 SDK 回调方法
     */
    override fun onReceiveLocation(location: BDLocation?) {
        location?.let {
            WeatherApplication.log(this, "location recall success : location is ${it.longitude} ${it.latitude} & " +
                        "locType is ${it.locType}"
            )
            place.value = BDPlace(
                "${it.city} ${it.street}",
                Location(it.longitude.toString(), it.latitude.toString()),
                it.locType
            )
        }
    }
}