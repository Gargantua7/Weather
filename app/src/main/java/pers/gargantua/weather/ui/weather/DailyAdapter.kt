package pers.gargantua.weather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.R
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.model.Weather
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Gargantua丶
 **/
class DailyAdapter(private val context: CityFragment, private val daily: Weather.Daily) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val daycon: ImageView = view.findViewById(R.id.daycon)
        val dayText: TextView = view.findViewById(R.id.dayText)
        val aqiText: TextView = view.findViewById(R.id.aqiText)
        val dayTemp: TextView = view.findViewById(R.id.dayTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.daily, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val map = daily[position]
        val day = Calendar.getInstance().run {
            time = Date()
            val day = get(Calendar.DAY_OF_WEEK) - 1
            if (day < 0) 0
            else day
        }
        holder.apply {
            daycon.setImageResource(Sky[map["skycon"] as String].icon)
            dayTemp.text = "${(map["minTemp"] as Double).roundToInt()} / ${(map["maxTemp"] as Double).roundToInt()} ℃"
            aqiText.text = (map["aqi"] as Double).toAqi()
            dayText.text = when (position) {
                0 -> context.getString(R.string.today)
                1 -> context.getString(R.string.tomorrow)
                else -> context.getString(GlobalData.weekday[(day + position) % 7])
            } + " · " + Sky[map["skycon"] as String].info
        }
    }

    override fun getItemCount(): Int = daily.temperature.size

}