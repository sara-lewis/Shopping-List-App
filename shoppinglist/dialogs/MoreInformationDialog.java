package us.ait.android.shoppinglist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.Grocery;

public class MoreInformationDialog extends DialogFragment {

    private ImageView groceryImage;
    private TextView tvgroceryItem;
    private TextView tvgroceryCost;
    private CheckBox checkBox;
    private TextView tvDescription;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.additional_info);
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.more_information, null);

        initViews(rootView);
        setInitializedViews();
        builder.setView(rootView);

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    private void setInitializedViews() {
        Grocery groceryToDisplay = (Grocery) getArguments().getSerializable(MainActivity.GROCERY_TO_DISPLAY);
        tvgroceryItem.setText(groceryToDisplay.getGroceryName());
        tvgroceryCost.setText(String.format(getString(R.string.dollar), Double.toString(groceryToDisplay.getEstimatedPrice())));
        tvDescription.setText(groceryToDisplay.getGroceryDescription());
        checkBox.setChecked(groceryToDisplay.isAlreadyPurchased());
        groceryImage.setImageResource(groceryToDisplay.getGroceryTypeAsEnum().getIconId());
    }

    private void initViews(View rootView) {
        groceryImage = (ImageView) rootView.findViewById(R.id.groceryImage);
        tvgroceryItem = (TextView) rootView.findViewById(R.id.tvgroceryItem);
        tvgroceryCost = (TextView) rootView.findViewById(R.id.tvgroceryCost);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
    }

}
