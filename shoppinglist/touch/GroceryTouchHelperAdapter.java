package us.ait.android.shoppinglist.touch;

public interface GroceryTouchHelperAdapter {

    public void onGroceryDismissed(int position);

    public void onGroceryMoved(int fromPosition, int toPosition);

}
