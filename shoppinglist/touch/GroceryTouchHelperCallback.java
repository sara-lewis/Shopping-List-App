package us.ait.android.shoppinglist.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import us.ait.android.shoppinglist.touch.GroceryTouchHelperAdapter;

public class GroceryTouchHelperCallback extends ItemTouchHelper.Callback {

    private GroceryTouchHelperAdapter groceryTouchHelperAdapter;

    public GroceryTouchHelperCallback(GroceryTouchHelperAdapter groceryTouchHelperAdapter) {
        this.groceryTouchHelperAdapter = groceryTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        groceryTouchHelperAdapter.onGroceryMoved(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        groceryTouchHelperAdapter.onGroceryDismissed(viewHolder.getAdapterPosition());
    }
}