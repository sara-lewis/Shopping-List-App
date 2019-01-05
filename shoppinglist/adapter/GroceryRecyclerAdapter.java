package us.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.AppDatabase;
import us.ait.android.shoppinglist.data.Grocery;
import us.ait.android.shoppinglist.touch.GroceryTouchHelperAdapter;

public class GroceryRecyclerAdapter extends RecyclerView.Adapter<GroceryRecyclerAdapter.ViewHolder> implements GroceryTouchHelperAdapter {

    private List<Grocery> groceryList;
    private Context context;

    public GroceryRecyclerAdapter(Context context, List<Grocery> groceryList){
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryRowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(groceryRowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Grocery grocery = groceryList.get(position);
        holder.groceryItem.setText(grocery.getGroceryName());
        holder.groceryCost.setText(String.format(context.getString(R.string.dollar), Double.toString(grocery.getEstimatedPrice())));
        holder.checkBox.setChecked(grocery.isAlreadyPurchased());
        holder.groceryImage.setImageResource(groceryList.get(position).getGroceryTypeAsEnum().getIconId());
        initButtons(holder, grocery);
    }

    private void initButtons(@NonNull ViewHolder holder, Grocery grocery) {
        initViewMoreDetailsButton(holder);
        initDeleteButton(holder, grocery);
        initCheckBox(holder, grocery);
        initEditButton(holder);
    }

    private void initEditButton(@NonNull final ViewHolder holder) {
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditGroceryDialog(groceryList.get(holder.getAdapterPosition()));
            }
        });
    }

    private void initViewMoreDetailsButton(@NonNull final ViewHolder holder) {
        holder.btnViewItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show more details here
                ((MainActivity) context).showMoreInformationDialog(groceryList.get(holder.getAdapterPosition()));
            }
        });
    }

    private void initDeleteButton(@NonNull ViewHolder holder, final Grocery grocery){
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGrocery(findGroceryIndexByGroceryId(grocery.getGroceryId()));
            }
        });
    }

    private void initCheckBox(@NonNull final ViewHolder holder, final Grocery grocery) {
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grocery.setAlreadyPurchased(holder.checkBox.isChecked());

                new Thread(){
                    @Override
                    public void run() {
                        AppDatabase.getAppDatabase(context).groceryDao().updateGrocery(grocery);
                    }
                }.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public void addGrocery(Grocery grocery) {
        groceryList.add(grocery);
        notifyDataSetChanged();
    }

    private void removeGrocery(int position) {
        final Grocery groceryToDelete = groceryList.get(position);
        new Thread(){
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).groceryDao().deleteGrocery(groceryToDelete);
            }
        }.start();

        groceryList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllGroceries(){
        while(groceryList.size() > 0){
            removeGrocery(0);
        }
    }

    @Override
    public void onGroceryDismissed(int position) {
        removeGrocery(position);
    }

    @Override
    public void onGroceryMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(groceryList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(groceryList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void updateGrocery(Grocery grocery) {
        int pos = findGroceryIndexByGroceryId(grocery.getGroceryId());
        if(pos != -1){
            groceryList.set(pos, grocery);
            notifyItemChanged(pos);
        }
    }

    public void sortGroceryListByPrice(){

        Collections.sort(groceryList, new Comparator<Grocery>() {
            @Override
            public int compare(Grocery grocery2, Grocery grocery1) {
                return  (int) (grocery1.getEstimatedPrice() - grocery2.getEstimatedPrice());
            }
        });

        notifyDataSetChanged();
    }


    private int findGroceryIndexByGroceryId(long groceryId){
        for (int i = 0; i < groceryList.size(); i++) {
            if(groceryList.get(i).getGroceryId() == groceryId){
                return i;
            }
        }
        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView groceryImage;
        private TextView groceryItem;
        private TextView groceryCost;
        private CheckBox checkBox;
        private Button btnViewItemDetails;
        private Button btnEdit;
        private ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            groceryImage = itemView.findViewById(R.id.groceryImage);
            groceryItem = itemView.findViewById(R.id.groceryItem);
            groceryCost = itemView.findViewById(R.id.groceryCost);
            checkBox = itemView.findViewById(R.id.checkBox);
            btnViewItemDetails = itemView.findViewById(R.id.btnViewItemDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }

}