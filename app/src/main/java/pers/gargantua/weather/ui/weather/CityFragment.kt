package pers.gargantua.weather.ui.weather

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.city.*
import kotlinx.android.synthetic.main.life.*
import kotlinx.android.synthetic.main.more.*
import kotlinx.android.synthetic.main.now.*
import pers.gargantua.weather.R
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import pers.gargantua.weather.logic.GlobalData.toBeaufortScale
import pers.gargantua.weather.logic.model.*
import pers.gargantua.weather.ui.view.ViewPagerPageChangeListener
import kotlin.math.roundToInt


/**
 * @author Gargantua丶
 **/
class CityFragment(private val position: Int, private val weatherActivity: WeatherActivity) : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(CityViewModel::class.java) }

    private val dailyAdapter by lazy { viewModel.weather?.let { DailyAdapter(this, it.result.daily) } }

    private val hourlyAdapter by lazy {
        viewModel.weather?.let {
            HourlyAdapter(
                this,
                it.result.hourly,
                it.timezone
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.city, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("gargantua-log", "fragment created: position - $position")

        if (GlobalData.places.size > 0) {
            val place = GlobalData.places[position]
            viewModel.placeName = place.name
            Log.d("gargantua-log", place.name)
            viewModel.getWeather(place.location.lng, place.location.lat)
            place.weather?.let {
                viewModel.weather = it
                refresh(it)
            }
        }
        Log.d("gargantua-log", "fragment old data got: position - $position")

        if (position == 0) {
            // 首页需要定位
            Log.d("gargantua-log", "location positioning")

            // 开始定位
            swipeRefresh.isRefreshing = true
            weatherActivity.locationClient.start()

            // 监听地理位置数据变化
            weatherActivity.locationListener.place.observe(viewLifecycleOwner) {
                if ("16" in it.status.toString()) {
                    if (ViewPagerPageChangeListener.position.value == 0)
                        weatherActivity.placeName.text = it.name
                    viewModel.placeName = it.name
                    viewModel.getWeather(it.location.lng, it.location.lat)
                    if (GlobalData.places.size == 0) {
                        if(weatherActivity.pages == 0)
                            weatherActivity.refreshTitleText(it.name)
                        GlobalData.places.add(
                            DaoPlace(
                                "",
                                it.name,
                                Location(it.location.lng, it.location.lat)
                            )
                        )
                    }
                } else {
                    Toast.makeText(activity, "定位失败", Toast.LENGTH_SHORT).show()
                }
                weatherActivity.locationClient.stop()
                swipeRefresh.isRefreshing = false
            }

            // 添加刷新，首页需要定位
            swipeRefresh.setOnRefreshListener {
                Log.d("gargantua-log", "Fragment - $position : Swipe Refresh")
                weatherActivity.locationClient.start()
            }
        } else {
            GlobalData.places[position].apply {
                Log.d("gargantua-log-id", "${this@CityFragment}-$id")
                viewModel.id = id
                viewModel.placeName = name
                viewModel.getWeather(location.lng, location.lat)
            }

            // 添加刷新，不需要定位
            swipeRefresh.setOnRefreshListener {
                Log.d("gargantua-log", "Fragment - $position : Swipe Refresh")
                viewModel.getWeather()
            }
        }

        Log.d("gargantua-log", "fragment old data screen: position - $position")

        viewModel.weatherLiveData.observe(viewLifecycleOwner) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                viewModel.weather = weather

                refresh(weather)

                dailyAdapter?.notifyDataSetChanged()
                hourlyAdapter?.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        }


        daily.layoutManager = object : LinearLayoutManager(weatherActivity) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        hourly.layoutManager = LinearLayoutManager(weatherActivity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        swipeRefresh.setProgressViewOffset(
            true, 50,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics).toInt()
        )
    }

    /**
     * 更新页面所有数据
     */
    private fun refresh(weather: Weather) {

        GlobalData.places[position].apply {
            name = viewModel.placeName
            location = viewModel.getLocationAtVM()
            this.weather = weather
        }

        Log.d("gargantua-log", "fragment data refresh start: position - $position")

        daily.adapter = dailyAdapter

        hourly.adapter = hourlyAdapter

        weather.result.realtime.apply {
            swipeRefresh.setColorSchemeResources(Sky[skycon].color)

            swipeRefresh.setBackgroundColor(weatherActivity.getColor(Sky[skycon].color))
            now.background = weatherActivity.getDrawable(Sky[skycon].bg)

            currentTemp.text = temperature.roundToInt().toString()
            currentSky.text = Sky[skycon].info
            aqi.text = "${getString(R.string.aqi)}${airQuality.aqi.chn.toAqi()}\t" +
                    "${airQuality.aqi.chn.roundToInt()}"

            forecast_keypoint.text = weather.result.forecastKeypoint

            more_wind_speed.text = wind.speed.toBeaufortScale().toString() +
                    getString(R.string.wind_speed)
            more_wind_direction.text =
                getString(GlobalData.windDirection[(wind.direction.roundToInt() + 20) / 40 % 8])
            more_humidity.text = (humidity * 100).roundToInt().toString() + "%"
            more_apparent_temperature.text = apparentTemperature.roundToInt().toString() + "℃"
            more_pressure.text = (pressure.roundToInt() / 100).toString() + "hPa"

        }
        val map = weather.result.daily[0]
        coldrisk.text = getString(GlobalData.coldRisk[map["coldRisk"] as Int - 1])
        dressing.text = getString(GlobalData.dressing[map["dressing"] as Int])
        ultraviolet.text = getString(GlobalData.ultraviolet[map["ultraviolet"] as Int - 1])
        carwashing.text = getString(GlobalData.carWashing[map["carWashing"] as Int - 1])

        Log.d("gargantua-log", "fragment data refresh finish: position - $position")
    }
}