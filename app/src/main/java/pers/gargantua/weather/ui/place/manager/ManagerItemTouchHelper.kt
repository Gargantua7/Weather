package pers.gargantua.weather.ui.place.manager

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pers.gargantua.weather.WeatherApplication

/**
 * 城市管理 RecyclerView 中的项目移动的回调类
 * @param adapter 城市管理 RecyclerView 的适配器
 * @author Gargantua丶
 **/
class ManagerItemTouchHelper(private val adapter: ManagerRecyclerAdapter) : ItemTouchHelper.Callback() {

    /** 监听项目上下左右四个方向的移动，但屏蔽定位项的监听 */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
        if (viewHolder.adapterPosition != 0)
            makeMovementFlags(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        else makeMovementFlags(0, 0)

    /**
     * 上下移动项目的回调方法
     * @see ItemTouchHelper.Callback.onMove
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        WeatherApplication.log(this.javaClass, "Item ${viewHolder.adapterPosition} Moving to ${target.adapterPosition}")
        // 移动后位置不能为顶位，即不能比定位项高
        if (target.adapterPosition != 0)
            adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * 左右滑动的回调方法
     * @see ItemTouchHelper.Callback.onSwiped
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        WeatherApplication.log(this.javaClass, "Item ${viewHolder.adapterPosition} Removed")
        adapter.onRemove(viewHolder.adapterPosition)
    }
}