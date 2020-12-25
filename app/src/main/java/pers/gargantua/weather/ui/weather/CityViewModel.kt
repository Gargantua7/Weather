package pers.gargantua.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pers.gargantua.weather.logic.Repository
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Weather

/**
 * [CityFragment] 的 ViewModel 层，用于保存临时数据
 * @author Gargantua丶
 **/
class CityViewModel: ViewModel() {

    /** LiveData 对象，监听 Location 对象变化*/
    private val locationLiveData = MutableLiveData<Location>()

    var weather: Weather? = null

    var id = ""

    var location = Location("", "")

    var placeName = ""

    /** 监听 [locationLiveData] 变化，并将异步处理后的 LiveData 对象赋值给本值 */
    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.getWeather(it)
    }

    /** 更新 [locationLiveData] 的参数，即更新后就会拉取新天气数据 */
    fun getWeather() {
        locationLiveData.value = location
    }

    /** 按方法参数更新 [locationLiveData] */
    fun getWeather(location: Location) {
        this.location = location
        getWeather()
    }
}