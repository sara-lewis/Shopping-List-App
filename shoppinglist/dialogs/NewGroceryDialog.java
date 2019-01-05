package us.ait.android.shoppinglist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.Grocery;

public class NewGroceryDialog extends DialogFragment{
    public interface GroceryHandler {
        // interface will be implemented inside the class where the information is being sent (mainActivity)
        public void groceryCreated(Grocery grocery);

        public void groceryUpdated(Grocery grocery);
    }

    private GroceryHandler groceryHandler;
    private Spinner spinnerGroceryType;
    private EditText etItemName;
    private EditText etEstimatedPrice;
    private EditText etDescription;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GroceryHandler){
            groceryHandler = (GroceryHandler) context;
        }else{
            throw new RuntimeException(getString(R.string.runtime_exception));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_new);
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.add_item_page, null);
        initDialogUI(rootView);
        initUIText(builder);
        builder.setView(rootView);
        initPositiveBtn(builder);
        return builder.create();
    }

    private void initPositiveBtn(AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void initUIText(AlertDialog.Builder builder) {
        if (getArguments() != null && getArguments().containsKey(MainActivity.KEY_GROCERY_TO_EDIT)) {
            builder.setTitle(R.string.edit_item);
            Grocery groceryToEdit = (Grocery) getArguments().getSerializable(MainActivity.KEY_GROCERY_TO_EDIT);
            etItemName.setText(groceryToEdit.getGroceryName());
            etEstimatedPrice.setText(Double.toString(groceryToEdit.getEstimatedPrice()));
            etDescription.setText(groceryToEdit.getGroceryDescription());
            spinnerGroceryType.setSelection(groceryToEdit.getGroceryTypeAsEnum().getValue());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initDialogUI(View rootView) {
        spinnerGroceryType = (Spinner) rootView.findViewById(R.id.spinnerGroceryType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.grocery_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroceryType.setAdapter(adapter);
        etItemName = (EditText) rootView.findViewById(R.id.etItemName);
        etEstimatedPrice = (EditText) rootView.findViewById(R.id.etEstimatedPrice);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(etItemName.getText())){
                        etItemName.setError(getString(R.string.error_msg));
                    }else if(TextUtils.isEmpty(etEstimatedPrice.getText())){
                        etEstimatedPrice.setError(getString(R.string.error_msg));
                    }else{
                        initWhenTextFilledIn(d);
                    }

                }
            });
        }
    }

    private void initWhenTextFilledIn(AlertDialog d) {
        if (getArguments() != null && getArguments().containsKey(MainActivity.KEY_GROCERY_TO_EDIT)) {
            initPreviousText();
        } else {
            Grocery grocery = new Grocery(
                    etItemName.getText().toString(),
                    Double.parseDouble(etEstimatedPrice.getText().toString()),
                    etDescription.getText().toString(),
                    false,
                    spinnerGroceryType.getSelectedItemPosition()
            );
            groceryHandler.groceryCreated(grocery);
        }

        d.dismiss();
    }

    private void initPreviousText() {
        Grocery groceryToEdit = (Grocery) getArguments().getSerializable(MainActivity.KEY_GROCERY_TO_EDIT);
        groceryToEdit.setGroceryName(etItemName.getText().toString());
        groceryToEdit.setEstimatedPrice(Double.parseDouble(etEstimatedPrice.getText().toString()));
        groceryToEdit.setGroceryDescription(etDescription.getText().toString());
        groceryToEdit.setGroceryType(spinnerGroceryType.getSelectedItemPosition());
        groceryHandler.groceryUpdated(groceryToEdit);
    }

}
