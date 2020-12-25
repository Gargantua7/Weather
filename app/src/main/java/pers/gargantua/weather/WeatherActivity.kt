package pers.gargantua.weather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import kotlinx.android.synthetic.main.activity_weather.*
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.Repository
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.network.LocationListener
import pers.gargantua.weather.ui.place.PlaceActivity
import pers.gargantua.weather.ui.view.MyScrollView
import pers.gargantua.weather.ui.weather.CityViewPagerAdapter
import pers.gargantua.weather.ui.weather.CityViewPagerPageChangeListener

/**
 * Launcher Activity
 *
 * 用于展示主页天气
 *
 * @author Gargantua丶
 */
class WeatherActivity : FragmentActivity() {

    /** 百度地图 SDK 服务 */
    val locationClient by lazy { LocationClient(this) }

    /** 百度地图 SDK 服务定位回调对象 */
    val locationListener = LocationListener()

    /** ViewPager 适配器 */
    private val viewPagerAdapter by lazy { CityViewPagerAdapter(this) }

    /** 当前 ScrollView 位置是否需要标题底色 */
    private var opaque: Boolean = false

    /** 当前 ViewPager 位置*/
    var pages: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 使状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        WeatherApplication.log(this, "onCreate")

        // 判断本地是否有保存的数据，有则提取
        if (Repository.isSaved())
            Repository.getSave()
        // 更新标题
        refreshTitleText()

        // 检查/申请定位权限
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            WeatherApplication.log(this.javaClass, "Request for Permission")
            requestPermissions(
                listOf(
                    ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION
                ).toTypedArray(), 1
            )
        }

        // 设置百度地图 SDK 定位服务参数
        locationClient.locOption = LocationClientOption().apply {
            // 设置为需要地址信息
            setIsNeedAddress(true)
            // 设置为返回最新的地址信息
            setNeedNewVersionRgc(true)
        }
        // 为百度地图 SDK 服务设置回调类
        locationClient.registerLocationListener(locationListener)

        WeatherApplication.log(this.javaClass, "Location Option Configuration Success")

        // 配置 ViewPager 适配器
        weather_view_pager.adapter = viewPagerAdapter
        // 配置 ViewPager 同时缓存页面为 10 (即设定最大，方便于多页同时滑动)
        weather_view_pager.offscreenPageLimit = 10
        // 为 ViewPager 设置页面更改监听
        weather_view_pager.addOnPageChangeListener(CityViewPagerPageChangeListener())

        // 监听 ViewPager 变更以修改地址标题
        CityViewPagerPageChangeListener.position.observe(this) {
            pages = it
            if (pages < viewPagerAdapter.count) {
                refreshTitleColor()
                refreshTitleText()
            }
        }

        // 监听 ScrollView 滑动距离以在滑动到一定距离后使标题栏透明度变化
        MyScrollView.opaque.observe(this) {
            opaque = it
            refreshTitleColor()
        }

        // 监听按钮
        weather_list_button.setOnClickListener {
            startActivityForResult(Intent(this, PlaceActivity::class.java), 1)
        }
    }

    /** 更新标题栏地址 */
    private fun refreshTitleText() {
        if (pages < GlobalData.places.size)
            refreshTitleText(GlobalData.places[pages].name)
    }

    /**
     * 更新标题栏地址
     * @param title 更新后展示的地址
     */
    fun refreshTitleText(title: String) {
        weather_place_name.text = title
    }

    /** 更新标题透明度和主题色 */
    private fun refreshTitleColor() {
        if (pages < GlobalData.places.size)
            GlobalData.places[pages].weather?.let {
                weather_place_name.setBackgroundColor(
                    getColor(
                        if (opaque)
                            Sky[it.result.realtime.skycon].color
                        else
                            R.color.transparent
                    )
                )
            }
    }

    /**
     * 接收 [PlaceActivity] 返回的值，用于在 PlaceActivity 中点击城市跳转
     * @see android.app.Activity.onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        WeatherApplication.log(
            this.javaClass,
            "onActivityResult{requestCode: $requestCode, resultCode: $resultCode}"
        )
        // 更新数据
        viewPagerAdapter.notifyDataSetChanged()
        refreshTitleText()
        // 判断返回码
        when (requestCode) {
            1 -> {
                // 判断结果码
                if (resultCode == RESULT_OK) {
                    val pages = data?.getIntExtra("pages", 0) ?: 0
                    weather_view_pager.currentItem = pages
                    WeatherApplication.log(
                        this.javaClass,
                        "onActivityResult: pager's page jumped to $pages"
                    )
                }
            }
        }
    }

    /**
     * Activity 销毁时保存数据
     */
    override fun onDestroy() {
        super.onDestroy()
        locationClient.stop()
        locationClient.unRegisterLocationListener(locationListener)
        WeatherApplication.log(this.javaClass, "Save And onDestroy")
        Repository.save()
    }
}