package pers.gargantua.weather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.model.Weather
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import java.util.*
import kotlin.math.roundToInt

/**
 * 天级预报 RecyclerView 适配器类
 * @param context [CityFragment] 的上下文
 * @param daily 天气结果中的天级预报部分
 * @author Gargantua丶
 * @see RecyclerView.Adapter
 **/
class CityDailyRecyclerAdapter(private val context: CityFragment, private val daily: Weather.Daily) :
    RecyclerView.Adapter<CityDailyRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val daycon: ImageView = view.findViewById(R.id.daily_daycon)
        val dayText: TextView = view.findViewById(R.id.daily_day_text)
        val aqiText: TextView = view.findViewById(R.id.daily_aqi)
        val dayTemp: TextView = view.findViewById(R.id.daily_realtime_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        WeatherApplication.log(this.javaClass, "onBindViewHolder: Position-$position")

        // 获取数据
        val map = daily[position]
        // 计算周几
        val day = Calendar.getInstance().run {
            time = Date()
            val day = get(Calendar.DAY_OF_WEEK) - 1
            if (day < 0) 0
            else day
        }
        // 展示数据
        holder.apply {
            daycon.setImageResource(Sky[map["skycon"] as String].icon)
            dayTemp.text = "${(map["minTemp"] as Double).roundToInt()} / ${(map["maxTemp"] as Double).roundToInt()} ℃"
            aqiText.text = (map["aqi"] as Double).toAqi(GlobalData.AQI_SHORT_DESCRIPTION)
            dayText.text = when (position) {
                0 -> context.getString(R.string.today)
                1 -> context.getString(R.string.tomorrow)
                else -> context.getString(GlobalData.weekday[(day + position) % 7])
            } + " · " + Sky[map["skycon"] as String].info
        }
    }

    override fun getItemCount(): Int = daily.temperature.size
}