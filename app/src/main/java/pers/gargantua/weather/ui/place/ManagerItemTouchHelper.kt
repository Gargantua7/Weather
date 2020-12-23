package pers.gargantua.weather.ui.place

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

/**
 * @author Gargantua丶
 **/
class ManagerItemTouchHelper(private val adapter: ManagerRecyclerAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
        if (viewHolder.adapterPosition != 0)
            makeMovementFlags(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
    else makeMovementFlags(0, 0)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
            Log.d("gargantua-log", "item ${viewHolder.adapterPosition} moving to ${target.adapterPosition}")
            if(target.adapterPosition != 0)
            adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onRemove(viewHolder.adapterPosition)
    }
}