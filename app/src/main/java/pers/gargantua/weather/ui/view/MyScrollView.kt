package pers.gargantua.weather.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.widget.ScrollView
import androidx.lifecycle.MutableLiveData
import pers.gargantua.weather.R
import pers.gargantua.weather.WeatherApplication

/**
 * 自定义 ScrollView，用于使 ViewPager 中每一页都能同步滑动，同时监听滑动高度，使标题栏正确变色
 * @author Gargantua丶
 * @see ScrollView
 **/
class MyScrollView : ScrollView {

    companion object {
        /** ScrollView 列表 */
        private val list = ArrayList<MyScrollView>()
        /** 标题栏是否需要更改透明度的 LiveData 对象 */
        val opaque = MutableLiveData<Boolean>()
    }

    constructor(context: Context) : super(context) {
        list.add(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        list.add(this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        list.add(this)
    }

    /** 多页同步滑动关键方法 */
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val scale = WeatherApplication.context.resources.displayMetrics.density
        val boolean = t > (450 * scale + 0.5f)
        // 检测滚动到标题栏需要更改透明度的位置时更改 opaque 的值
        if (opaque.value != boolean) opaque.value = boolean
        for (i in list) {
            if (i != this) {
                // 使其他页也滑动到当前位置
                i.scrollTo(l, t)
            }
        }
    }
}