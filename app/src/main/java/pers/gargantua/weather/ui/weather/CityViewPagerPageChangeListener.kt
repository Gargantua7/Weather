package pers.gargantua.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager

/**
 * 监听 ViewPager 页面滑动
 * @author Gargantua丶
 **/
class CityViewPagerPageChangeListener: ViewPager.OnPageChangeListener {

    companion object {
        /** 页面滑动将被反馈到当前 LiveData 对象 */
        val position = MutableLiveData<Int>()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(positionN: Int) {
        position.value = positionN
    }

    override fun onPageScrollStateChanged(state: Int) {}
}