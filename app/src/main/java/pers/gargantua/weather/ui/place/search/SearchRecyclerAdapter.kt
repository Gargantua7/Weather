package pers.gargantua.weather.ui.place.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Place
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.ui.place.PlaceActivity
import kotlin.math.roundToInt

/**
 * 查询城市 RecyclerView 适配器
 * @param context [PlaceActivity] 上下文
 * @param placeList 数据列表
 * @author Gargantua丶
 * @see RecyclerView.Adapter
 **/
class SearchRecyclerAdapter(private val context: PlaceActivity, private val placeList: List<Place>) :
    RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val add: Button = view.findViewById(R.id.place_search_add_button)
        val placeName: TextView = view.findViewById(R.id.place_search_name)
        val address: TextView = view.findViewById(R.id.place_search_address)
        val icon: List<ImageView> = ArrayList<ImageView>().apply {
            add(view.findViewById(R.id.place_search_today_weather_icon))
            add(view.findViewById(R.id.place_search_tomorrow_weather_icon))
            add(view.findViewById(R.id.place_search_day_after_tomorrow_weather_icon))
        }
        val max: List<TextView> = ArrayList<TextView>().apply {
            add(view.findViewById(R.id.place_search_today_max))
            add(view.findViewById(R.id.place_search_tomorrow_max))
            add(view.findViewById(R.id.place_search_day_after_tomorrow_max))
        }
        val min: List<TextView> = ArrayList<TextView>().apply {
            add(view.findViewById(R.id.place_search_today_min))
            add(view.findViewById(R.id.place_search_tomorrow_min))
            add(view.findViewById(R.id.place_search_day_after_tomorrow_min))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place_search, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        WeatherApplication.log(this.javaClass, "onBindViewHolder: Position-$position")

        // 获取并更新数据
        val place = placeList[position]
        holder.apply {
            val name = if (place.name.length < place.address.length) place.name else place.address
            val ars = if (place.name.length < place.address.length) place.address else place.name
            placeName.text = name
            address.text = ars
            place.weather?.let {
                for (i in 0..2) {
                    icon[i].setImageResource(Sky[it.result.daily[i]["skycon"] as String].icon)
                    max[i].text = "${(it.result.daily[i]["maxTemp"] as Double).roundToInt()}℃"
                    min[i].text = "${(it.result.daily[i]["minTemp"] as Double).roundToInt()}℃"
                }
            }

            // 初始化按钮背景
            add.background = WeatherApplication.context.getDrawable(R.drawable.add)
            for (p in GlobalData.places) {
                if (place.id == p.id) {
                    // 如果城市已添加，将按钮背景改为 √
                    add.background = WeatherApplication.context.getDrawable(R.drawable.check)
                    // 添加按下监听
                    add.setOnClickListener {
                        Toast.makeText(context, "城市已在列表中", Toast.LENGTH_SHORT).show()
                    }
                    // 结束此 lambda
                    return@apply
                }
            }


            // 为按钮添加按下监听器
            add.setOnClickListener {
                // 检测是否超出上限，是则提示，并结束此 lambda
                if (GlobalData.places.size >= 10) {
                    Toast.makeText(context, "添加城市数已达上限", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // 添加城市
                GlobalData.places.add(
                    DaoPlace(
                        place.id,
                        name,
                        Location(place.location.lng, place.location.lat),
                        place.weather
                    )
                )
                // 发出提示，并更改背景
                Toast.makeText(context, "城市添加成功", Toast.LENGTH_SHORT).show()
                add.background = WeatherApplication.context.getDrawable(R.drawable.check)
            }
        }
    }

    override fun getItemCount() = placeList.size
}
