package pers.gargantua.weather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.GlobalData.toAqi
import pers.gargantua.weather.logic.GlobalData.toBeaufortScale
import pers.gargantua.weather.logic.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * 小时级预报 RecyclerView 适配器类
 * @param context [CityFragment] 的上下文
 * @param hourly 天气结果中的小时级预报部分
 * @author Gargantua丶
 * @see RecyclerView.Adapter
 **/
class CityHourlyRecyclerAdapter(private val context: CityFragment, private val hourly: Weather.Hourly, private val timezone: String) :
    RecyclerView.Adapter<CityHourlyRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.hourly_time)
        val temp: TextView = view.findViewById(R.id.hourly_temp)
        val icon: ImageView = view.findViewById(R.id.hourly_weather_icon)
        val windDirection: ImageView = view.findViewById(R.id.hourly_wind_direction)
        val windSpeed: TextView = view.findViewById(R.id.hourly_wind_speed)
        val aqi: TextView = view.findViewById(R.id.hourly_aqi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hourly, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        WeatherApplication.log(this.javaClass, "onBindViewHolder: Position-$position")

        // 获取数据
        val map = hourly[position]
        holder.apply {
            // 计算展示时间
            time.text = if (position > 0) {
                // 未来的预报
                val date = SimpleDateFormat("HH:00").run {
                    timeZone = TimeZone.getTimeZone(this@CityHourlyRecyclerAdapter.timezone)
                    format(map["time"] as Date)
                }
                // 如果日期为午夜 0 点，则显示日期
                if (date == "00:00") SimpleDateFormat("MM/dd").run {
                    timeZone = TimeZone.getTimeZone(this@CityHourlyRecyclerAdapter.timezone)
                    format(map["time"] as Date)
                }
                else date
            } else
                context.getString(R.string.now) // 当前预报，显示 '现在'
            // 展示数据
            temp.text = "${(map["temp"] as Double).roundToInt()}℃"
            icon.setImageResource(Sky[map["skycon"] as String].icon)
            windDirection.rotation = (map["windDirection"] as Double).toFloat() + 180F
            windSpeed.text = (map["windSpeed"] as Double).toBeaufortScale().toString() + context.getString(R.string.wind_speed)
            aqi.text = (map["aqi"] as Double).toAqi(GlobalData.AQI_SHORT_DESCRIPTION)
        }
    }

    override fun getItemCount(): Int = hourly.temperature.size
}