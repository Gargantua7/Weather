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
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.layout_life.*
import kotlinx.android.synthetic.main.layout_more.*
import kotlinx.android.synthetic.main.layout_now.*
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherActivity
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.GlobalData.toAqi
import pers.gargantua.weather.logic.GlobalData.toBeaufortScale
import pers.gargantua.weather.logic.model.*
import kotlin.math.roundToInt


/**
 * ViewPager 中的 Fragment 类
 * @param position 当前 Fragment 在 ViewPager 中的定位
 * @param weatherActivity [WeatherActivity] 上下文，用于定位服务
 * @author Gargantua丶
 **/
class CityFragment(private val position: Int, private val weatherActivity: WeatherActivity) : Fragment() {

    /** 当前 Fragment 的 ViewModel 对象 */
    private val viewModel by lazy { ViewModelProvider(this).get(CityViewModel::class.java) }

    /** 天级预报 RecyclerView 适配器 */
    private val dailyAdapter by lazy { viewModel.weather?.let { CityDailyRecyclerAdapter(this, it.result.daily) } }

    /** 小时级预报 RecyclerView 适配器 */
    private val hourlyAdapter by lazy {
        viewModel.weather?.let {
            CityHourlyRecyclerAdapter(
                this,
                it.result.hourly,
                it.timezone
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        WeatherApplication.log(this.javaClass, "onActivityCreated")

        // 如果有旧数据，则先读取旧数据并展示
        if (GlobalData.places.size > 0) {
            val place = GlobalData.places[position]
            viewModel.placeName = place.name
            Log.d("gargantua-log", place.name)
            viewModel.getWeather(Location(place.location.lng, place.location.lat))
            place.weather?.let {
                viewModel.weather = it
                refresh(it)
            }
        }

        WeatherApplication.log(this.javaClass, "Old-data Got")

        if (position == 0) {
            // 首页需要定位
            WeatherApplication.log(this.javaClass,"Location Positioning")

            // 开始定位
            city_swipe_refresh.isRefreshing = true
            weatherActivity.locationClient.start()

            // 监听地理位置数据变化
            weatherActivity.locationListener.place.observe(viewLifecycleOwner) {
                if ("161" in it.status.toString()) {
                    // 定位成功

                    // 更新当前页数据
                    if (CityViewPagerPageChangeListener.position.value == 0)
                        weatherActivity.weather_place_name.text = it.name
                    viewModel.placeName = it.name
                    viewModel.getWeather(Location(it.location.lng, it.location.lat))

                    // 如果全局数据为空 (第一次使用APP), 则创建一个新项作为定位项
                    if (GlobalData.places.size == 0) {
                        if (weatherActivity.pages == 0)
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
                    //定位失败
                    Toast.makeText(activity, "定位失败", Toast.LENGTH_SHORT).show()
                }
                // 停止定位
                weatherActivity.locationClient.stop()
                city_swipe_refresh.isRefreshing = false
            }

            // 添加刷新，首页需要定位
            city_swipe_refresh.setOnRefreshListener {
                WeatherApplication.log(this.javaClass,"Swipe Refresh")
                //开始定位
                weatherActivity.locationClient.start()
            }
        } else {
            // 拉取新数据并展示
            GlobalData.places[position].apply {
                viewModel.id = id
                viewModel.placeName = name
                viewModel.getWeather(Location(location.lng, location.lat))
            }

            // 添加刷新，不需要定位
            city_swipe_refresh.setOnRefreshListener {
                WeatherApplication.log(this.javaClass,"Swipe Refresh")
                viewModel.getWeather()
            }
        }

        WeatherApplication.log(this.javaClass, "Old-Data Screened")

        // 监听天气数据是否更新
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { result ->
            // 获取新天气
            val weather = result.getOrNull()
            // 判断非空
            if (weather != null) {
                // 更新天气数据
                viewModel.weather = weather

                refresh(weather)

                dailyAdapter?.notifyDataSetChanged()
                hourlyAdapter?.notifyDataSetChanged()
            } else {
                // 返回为空，获取数据失败
                Toast.makeText(activity, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            city_swipe_refresh.isRefreshing = false
        }

        // 为天级预报 RecyclerView 设定布局管理器为不可滚动的纵向线性布局
        city_daily_recycler_view.layoutManager = object : LinearLayoutManager(weatherActivity) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        // 为小时级预报 RecyclerView 设定布局管理器为横向线性布局
        city_hourly_recycler_view.layoutManager = LinearLayoutManager(weatherActivity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        // 设定 SwipeRefreshLayout 下拉刷新圆圈位置，目的是不遮住标题
        city_swipe_refresh.setProgressViewOffset(
            true, 50,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics).toInt()
        )
    }

    /**
     * 更新页面所有数据
     */
    private fun refresh(weather: Weather) {

        WeatherApplication.log(this.javaClass, "refresh: Date Save Start")

        GlobalData.places[position].apply {
            name = viewModel.placeName
            location = viewModel.location
            this.weather = weather
        }

        WeatherApplication.log(this.javaClass, "refresh: Date Saved And Refresh Start")

        city_daily_recycler_view.adapter = dailyAdapter

        city_hourly_recycler_view.adapter = hourlyAdapter

        weather.result.realtime.apply {
            city_swipe_refresh.setColorSchemeResources(Sky[skycon].color)

            city_swipe_refresh.setBackgroundColor(weatherActivity.getColor(Sky[skycon].color))
            now.background = weatherActivity.getDrawable(Sky[skycon].bg)

            now_temp.text = temperature.roundToInt().toString()
            now_sky.text = Sky[skycon].info
            now_aqi.text = "${getString(R.string.aqi)}${airQuality.aqi.chn.toAqi()}\t" +
                    "${airQuality.aqi.chn.roundToInt()}"

            city_forecast_keypoint.text = weather.result.forecastKeypoint

            more_wind_speed.text = wind.speed.toBeaufortScale().toString() +
                    getString(R.string.wind_speed)
            more_wind_direction.text =
                getString(GlobalData.getWindDirection(wind.direction))
            more_humidity.text = (humidity * 100).roundToInt().toString() + "%"
            more_apparent_temperature.text = apparentTemperature.roundToInt().toString() + "℃"
            more_pressure.text = (pressure.roundToInt() / 100).toString() + "hPa"

        }
        val map = weather.result.daily[0]
        life_coldrisk.text = getString(GlobalData.coldRisk[map["coldRisk"] as Int])
        dressing.text = getString(GlobalData.dressing[map["dressing"] as Int])
        life_ultraviolet.text = getString(GlobalData.ultraviolet[map["ultraviolet"] as Int])
        life_carwashing.text = getString(GlobalData.carWashing[map["carWashing"] as Int])

        WeatherApplication.log(this.javaClass,"Refresh: Finish")
    }

    /** 重写 toString，目的是便于打印日志 */
    override fun toString(): String {
        return super.toString() + "[position: $position]"
    }
}