package pers.gargantua.weather.ui.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import pers.gargantua.weather.logic.GlobalData

/**
 * @author Gargantua丶
 **/
class CityViewPagerAdapter(private val weatherActivity: WeatherActivity) :
    FragmentPagerAdapter(weatherActivity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = GlobalData.places.size.run { if(this == 0) 1 else this }

    override fun getItem(position: Int): Fragment = CityFragment(position, weatherActivity)

    override fun getItemPosition(obj: Any): Int = POSITION_NONE

}