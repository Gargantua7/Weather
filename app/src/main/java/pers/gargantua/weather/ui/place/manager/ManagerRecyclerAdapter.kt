package pers.gargantua.weather.ui.place.manager

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.ui.place.PlaceActivity
import java.util.*
import kotlin.math.roundToInt

/**
 * 管理城市 RecyclerView 适配器
 * @param context [PlaceActivity] 上下文
 * @author Gargantua丶
 * @see RecyclerView.Adapter
 **/
class ManagerRecyclerAdapter(val context: PlaceActivity) :
    RecyclerView.Adapter<ManagerRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.manager_item_place_name)
        val aqi: TextView = view.findViewById(R.id.manager_item_aqi)
        val temp: TextView = view.findViewById(R.id.manager_item_temp)
        val realtimeTemp: TextView = view.findViewById(R.id.manager_item_realtime_temp)
        val card: MaterialCardView = view.findViewById(R.id.item_place_manager_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place_manager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        WeatherApplication.log(this.javaClass, "onBindViewHolder: Position-$position")

        // 展示数据
        GlobalData.places[position].apply {
            holder.placeName.text = name.split(" ").run { this[this.size - 1] }
            weather?.let {
                holder.aqi.text = "${context.getString(R.string.aqi)}${it.result.realtime.airQuality.aqi.chn.toAqi()}"
                val map = it.result.daily[0]
                holder.temp.text =
                    "${(map["minTemp"] as Double).roundToInt()} / ${(map["maxTemp"] as Double).roundToInt()} " +
                            context.getString(R.string.temp_symbol)
                holder.realtimeTemp.text = it.result.realtime.temperature.roundToInt().toString()
                holder.card.setCardBackgroundColor(
                    context.getColor(Sky[it.result.realtime.skycon].color)
                )
            }
        }

        // 添加按下监听器，使返回当前城市的 position
        holder.card.setOnClickListener {
            context.apply {
                setResult(RESULT_OK, Intent().putExtra("pages", position))
                finish()
            }
        }
    }

    override fun getItemCount(): Int = GlobalData.places.size

    /**
     * 项目移动
     * @param formPosition 项目原位置
     * @param toPosition 移动后位置
     */
    fun onMove(formPosition: Int, toPosition: Int) {
        Collections.swap(GlobalData.places, formPosition, toPosition)
        notifyItemMoved(formPosition, toPosition)
    }

    /**
     * 项目删除
     * @param position 删除的项目的位置
     */
    fun onRemove(position: Int) {
        GlobalData.places.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "已删除此城市", Toast.LENGTH_SHORT).show()
    }

}