package pers.gargantua.weather.ui.place

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
import kotlin.math.roundToInt

/**
 * @author Gargantua丶
 **/
class SearchRecyclerAdapter(private val context: Context, private val placeList: List<Place>) :
    RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val add: Button = view.findViewById(R.id.add)
        val placeName: TextView = view.findViewById(R.id.place_name)
        val address: TextView = view.findViewById(R.id.address)
        val icon: List<ImageView> = ArrayList<ImageView>().apply {
            add(view.findViewById(R.id.today_weather_icon))
            add(view.findViewById(R.id.tomorrow_weather_icon))
            add(view.findViewById(R.id.day_after_tomorrow_weather_icon))
        }
        val max: List<TextView> = ArrayList<TextView>().apply {
            add(view.findViewById(R.id.today_max))
            add(view.findViewById(R.id.tomorrow_max))
            add(view.findViewById(R.id.day_after_tomorrow_max))
        }
        val min: List<TextView> = ArrayList<TextView>().apply {
            add(view.findViewById(R.id.today_min))
            add(view.findViewById(R.id.tomorrow_min))
            add(view.findViewById(R.id.day_after_tomorrow_min))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.apply {
            val name = if(place.name.length < place.address.length) place.name else place.address
            val ars = if(place.name.length < place.address.length) place.address else place.name
            placeName.text = name
            address.text = ars
            place.weather?.let {
                for (i in 0..2) {
                    icon[i].setImageResource(Sky[it.result.daily[i]["skycon"] as String].icon)
                    max[i].text = "${(it.result.daily[i]["maxTemp"] as Double).roundToInt()}℃"
                    min[i].text = "${(it.result.daily[i]["minTemp"] as Double).roundToInt()}℃"
                }
            }

            add.background = WeatherApplication.context.getDrawable(R.drawable.add_circle)
            for(p in GlobalData.places) {
                if(place.id == p.id) {
                    add.background = WeatherApplication.context.getDrawable(R.drawable.check)
                    break
                }
            }

            add.setOnClickListener {
                for(p in GlobalData.places) {
                    if(place.id == p.id) {
                        Toast.makeText(context, "城市已在列表中", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }

                GlobalData.places.add(
                    DaoPlace(
                        place.id.apply{
                            Log.d("gargantua-log-id", "${this@SearchRecyclerAdapter}-$this")
                        },
                        name,
                        Location(place.location.lng, place.location.lat),
                        place.weather
                    )
                )
                Log.d("gargantua-log-id", "$this-${GlobalData.places[GlobalData.places.size - 1].id}")
                Toast.makeText(context, "城市添加成功", Toast.LENGTH_SHORT).show()
                add.background = WeatherApplication.context.getDrawable(R.drawable.check)
            }
        }
    }

    override fun getItemCount() = placeList.size
}
