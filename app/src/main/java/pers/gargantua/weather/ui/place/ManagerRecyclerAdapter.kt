package pers.gargantua.weather.ui.place

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import pers.gargantua.weather.R
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import kotlin.math.roundToInt

/**
 * @author Gargantua丶
 **/
class ManagerRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<ManagerRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.manager_item_place_name)
        val aqi: TextView = view.findViewById(R.id.manger_item_aqi)
        val temp: TextView = view.findViewById(R.id.manger_item_temp)
        val realtimeTemp: TextView = view.findViewById(R.id.manager_item_realtime_temp)
        val card: MaterialCardView = view.findViewById(R.id.item_place_manager_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place_manager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
                    context.getColor((GlobalData.sky[it.result.realtime.skycon]?: error("")).color)
                )
            }
        }

        holder.card.setOnLongClickListener {
            if(position != 0) {
                AlertDialog.Builder(context).apply {
                    setMessage("确定删除${holder.placeName.text}？")
                    setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("确定") { dialog, _ ->
                        GlobalData.places.removeAt(position)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }.show()
            }
            true
        }
    }

    override fun getItemCount(): Int = GlobalData.places.size

}