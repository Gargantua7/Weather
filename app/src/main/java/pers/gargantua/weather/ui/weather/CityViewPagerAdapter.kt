package pers.gargantua.weather.ui.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import pers.gargantua.weather.WeatherActivity
import pers.gargantua.weather.logic.GlobalData

/**
 * ViewPager 适配器
 * @param weatherActivity WeatherActivity 上下文，用于传递给 [CityFragment]
 * @author Gargantua丶
 **/
class CityViewPagerAdapter(private val weatherActivity: WeatherActivity) :
    FragmentPagerAdapter(weatherActivity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = GlobalData.places.size.run { if(this == 0) 1 else this }

    override fun getItem(position: Int): Fragment = CityFragment(position, weatherActivity)

    /** 用于强制刷新 ViewPager */
    override fun getItemPosition(obj: Any): Int = POSITION_NONE

}