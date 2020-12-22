package pers.gargantua.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pers.gargantua.weather.logic.Repository
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Weather

/**
 * @author Gargantua丶
 **/
class CityViewModel: ViewModel() {

    private val locationLiveDate = MutableLiveData<Location>()

    var weather: Weather? = null

    var id = ""

    private var lng = ""

    private var lat = ""

    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveDate) {
        Repository.getWeather(it.lng, it.lat)
    }

    fun getWeather() {
        locationLiveDate.value = Location(lng, lat)
    }

    fun getWeather(lng: String, lat: String) {
        this.lng = lng
        this.lat = lat
        getWeather()
    }

    fun getLocationAtVM() = Location(lng, lat)

}