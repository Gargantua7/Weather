package pers.gargantua.weather.logic.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import pers.gargantua.weather.logic.model.BDPlace
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location

/**
 * @author Gargantua丶
 **/
class LocationListener : BDAbstractLocationListener() {

    val place = MutableLiveData<BDPlace>()

    override fun onReceiveLocation(location: BDLocation?) {
        location?.let {
            Log.d(
                "gargantua-log", "location recall success : location is ${it.longitude} ${it.latitude} & " +
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