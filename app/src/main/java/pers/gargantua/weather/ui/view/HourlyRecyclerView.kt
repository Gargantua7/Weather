package pers.gargantua.weather.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * @author Gargantua丶
 **/
class HourlyRecyclerView : RecyclerView {

    private var touchSlop: Int = 0

    constructor(context: Context) : super(context) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private var moveX = 0
    private var moveY = 0
    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                moveX = e.x.toInt()
                moveY = e.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val y = e.y.toInt()
                val x = e.x.toInt()
                if (abs(y - moveY) > touchSlop && abs(x - moveX) < touchSlop * 2) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.onTouchEvent(e)
    }
}