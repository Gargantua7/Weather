package pers.gargantua.weather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @author Gargantua丶
 **/
data class PlaceRepo(val status: String, val places: List<Place>)

data class DaoPlace(
    val id: String,
    val name: String,
    val location: Location,
    var weather: Weather? = null
)

data class Place(
    val id: String,
    val name: String,
    val location: Location,
    @SerializedName("formatted_address")
    val address: String,
    var weather: Weather? = null
)

data class BDPlace(
    val name: String,
    val location: Location,
    val status: Int
)

data class Location(
    val lng: String,
    val lat: String
)
