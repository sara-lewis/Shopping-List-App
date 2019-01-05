package us.ait.android.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import us.ait.android.shoppinglist.adapter.GroceryRecyclerAdapter;
import us.ait.android.shoppinglist.data.AppDatabase;
import us.ait.android.shoppinglist.data.Grocery;
import us.ait.android.shoppinglist.dialogs.MoreInformationDialog;
import us.ait.android.shoppinglist.dialogs.NewGroceryDialog;
import us.ait.android.shoppinglist.touch.GroceryTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements NewGroceryDialog.GroceryHandler{

    public static final String KEY_GROCERY_TO_EDIT = "KEY_GROCERY_TO_EDIT";
    public static final String GROCERY_TO_DISPLAY = "GROCERY_TO_DISPLAY";
    private GroceryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();
    }

    private void initUI() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Telling recycler view to use the adapter object that's been implemented in the GroceryRecyclerAdapter class
        RecyclerView recyclerViewGroceries = findViewById(R.id.recyclerShoppingList);
        recyclerViewGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroceries.setHasFixedSize(true);
        initRecyclerView(recyclerViewGroceries);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    public void showNewGroceryDialog(){
        new NewGroceryDialog().show(getFragmentManager(), getString(R.string.tag));
    }


    private void initRecyclerView(final RecyclerView recyclerViewGroceries) {

        new Thread(){
            @Override
            public void run() {
                final List<Grocery> groceries = AppDatabase.getAppDatabase(MainActivity.this).groceryDao().getAllGroceries();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new GroceryRecyclerAdapter(MainActivity.this, groceries);
                        recyclerViewGroceries.setAdapter(adapter);
                        // lets recycleView know that it should use the touchHelper
                        ItemTouchHelper.Callback callback = new GroceryTouchHelperCallback(adapter);
                        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                        touchHelper.attachToRecyclerView(recyclerViewGroceries);
                    }
                });
            }
        }.start();


    }

    public void showEditGroceryDialog(Grocery grocery) {
        NewGroceryDialog editPlaceDialog = new NewGroceryDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_GROCERY_TO_EDIT, grocery);
        editPlaceDialog.setArguments(bundle);
        editPlaceDialog.show(getFragmentManager(), getString(R.string.edit_tag));
    }

    public void showMoreInformationDialog(Grocery grocery){
        MoreInformationDialog moreInformationDialog = new MoreInformationDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GROCERY_TO_DISPLAY, grocery);
        moreInformationDialog.setArguments(bundle);
        moreInformationDialog.show(getFragmentManager(), getString(R.string.show));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAddItem:
                showNewGroceryDialog();
                break;
            case R.id.menuDeleteList:
                adapter.removeAllGroceries();
                break;
            case R.id.menuSort:
                adapter.sortGroceryListByPrice();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public void groceryCreated(final Grocery grocery) {
        new Thread(){
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).groceryDao().insertGrocery(grocery);
                grocery.setGroceryId(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addGrocery(grocery);
                    }
                });
            }
        }.start();
    }

    @Override
    public void groceryUpdated(final Grocery grocery) {
        new Thread(){
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).groceryDao().updateGrocery(grocery);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateGrocery(grocery);
                    }
                });
            }
        }.start();
    }
}