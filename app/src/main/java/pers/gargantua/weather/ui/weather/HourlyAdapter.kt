package pers.gargantua.weather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.R
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.GlobalData.toAqi
import pers.gargantua.weather.logic.GlobalData.toBeaufortScale
import pers.gargantua.weather.logic.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Gargantua丶
 **/
class HourlyAdapter(private val context: CityFragment, private val hourly: Weather.Hourly, private val timezone: String) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.hourly_time)
        val temp: TextView = view.findViewById(R.id.hourly_temp)
        val icon: ImageView = view.findViewById(R.id.hourly_weather_icon)
        val windDirection: ImageView = view.findViewById(R.id.hourly_wind_direction)
        val windSpeed: TextView = view.findViewById(R.id.hourly_wind_speed)
        val aqi: TextView = view.findViewById(R.id.hourly_aqi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hourly, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val map = hourly[position]
        holder.apply {
            time.text = if (position > 0) {
                val date = SimpleDateFormat("HH:00").run {
                    timeZone = TimeZone.getTimeZone(this@HourlyAdapter.timezone)
                    format(map["time"] as Date)
                }
                if (date == "00:00") SimpleDateFormat("MM/dd").run {
                    timeZone = TimeZone.getTimeZone(this@HourlyAdapter.timezone)
                    format(map["time"] as Date)
                }
                else date
            } else
                context.getString(R.string.now)
            temp.text = "${(map["temp"] as Double).roundToInt()}℃"
            icon.setImageResource(Sky[map["skycon"] as String].icon)
            windDirection.rotation = (map["windDirection"] as Double).toFloat() + 180F
            windSpeed.text = (map["windSpeed"] as Double).toBeaufortScale().toString() + context.getString(R.string.wind_speed)
            aqi.text = (map["aqi"] as Double).toAqi()
        }
    }

    override fun getItemCount(): Int = hourly.temperature.size
}