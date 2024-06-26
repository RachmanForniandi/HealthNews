package rachmanforniandi.healthnews.utility

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import rachmanforniandi.healthnews.adapters.HealthNewsAdapter

abstract class SwipeToDelete :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


}