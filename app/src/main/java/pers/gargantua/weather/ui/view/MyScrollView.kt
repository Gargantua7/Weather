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
 * @author Gargantua丶
 **/
class MyScrollView : ScrollView {

    companion object {
        private val list = ArrayList<MyScrollView>()
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

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val scale = WeatherApplication.context.resources.displayMetrics.density
        val boolean = t > (450 * scale + 0.5f)
        if (opaque.value != boolean) opaque.value = boolean
        for (i in list) {
            if (i != this) {
                i.scrollTo(l, t)
            }
        }
    }
}