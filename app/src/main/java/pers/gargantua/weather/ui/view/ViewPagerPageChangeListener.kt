package pers.gargantua.weather.ui.view

import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager

/**
 * @author Gargantua丶
 **/
class ViewPagerPageChangeListener: ViewPager.OnPageChangeListener {

    companion object {
        val position = MutableLiveData<Int>()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(positionN: Int) {
        position.value = positionN
    }

    override fun onPageScrollStateChanged(state: Int) {}
}