package pers.gargantua.weather.ui.weather

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.place_item.view.*
import pers.gargantua.weather.R
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Sky
import pers.gargantua.weather.logic.network.LocationListener
import pers.gargantua.weather.ui.place.PlaceActivity
import pers.gargantua.weather.ui.view.MyScrollView
import pers.gargantua.weather.ui.view.ViewPagerPageChangeListener

class WeatherActivity : FragmentActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    val locationClient by lazy { LocationClient(this) }

    val locationListener = LocationListener()

    private val viewPagerAdapter by lazy {
        CityViewPagerAdapter(this).apply {
            Log.d("gargantua-log", "ViewPager has $count page(s)")
        }
    }

    private var opaque: Boolean = false
    private var pages: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        // 检查/申请定位权限
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION
                ).toTypedArray(), 1
            )
        }

        // 设置定位服务
        locationClient.locOption = LocationClientOption().apply {
            setIsNeedAddress(true)
            setNeedNewVersionRgc(true)
        }
        locationClient.registerLocationListener(locationListener)

        viewModel.restoreSave()

//        viewModel.places.add(
//            DaoPlace(
//                "Test",
//                Location("109.304", "18.1510")
//            )
//        )

        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 5
        viewPager.addOnPageChangeListener(ViewPagerPageChangeListener())

        ViewPagerPageChangeListener.position.observe(this) {
            pages = it
            if(pages < viewPagerAdapter.count) {
                placeName.text = GlobalData.places[pages].name
                refreshTitle()
            }
        }

        MyScrollView.opaque.observe(this) {
            opaque = it
            refreshTitle()
        }

        list_button.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java))
        }
    }

    private fun refreshTitle() {
        GlobalData.places[pages].weather?.let {
            placeName.setBackgroundColor(
                getColor(
                    if (opaque)
                        Sky[it.result.realtime.skycon].color
                    else
                        R.color.transparent
                )
            )
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewPagerAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.stop()
        locationClient.unRegisterLocationListener(locationListener)
        Log.d("gargantua-log", "save in activity")
        viewModel.save()
    }
}