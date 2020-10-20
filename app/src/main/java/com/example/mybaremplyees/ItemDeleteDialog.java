package com.example.mybaremplyees;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;

public class ItemDeleteDialog extends AppCompatDialogFragment {

    private MySPV mySPV;
    Gson gson = new Gson();

    private Item item;

    Order new_order = new Order();


    public ItemDeleteDialog() {
    }

    public ItemDeleteDialog(Item item) {
        this.item = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_itemdelete, null);

        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close dialog
            }
        }).setPositiveButton("remove", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mySPV = new MySPV(getActivity().getApplicationContext());
                // Inset item to current order on SP num_of_items times
                // Get order from SP
                new_order = getCurrentOrderFromSP();
                new_order.removeOneItem(item);
                insetNewOrderToSP(item);
                Activity_ViewOrder.getInstance().finish();
                Activity_ViewOrder.getInstance().start();
            }
        });
        return builder.create();
    }

    private void insetNewOrderToSP(Item item) {
        // Insert to SP
        String json_order = gson.toJson(new_order);
        mySPV.putString(MySPV.KEYS.CURRENT_ORDER, json_order);
    }

    private Order getCurrentOrderFromSP(){
        String json_order = mySPV.getString(MySPV.KEYS.CURRENT_ORDER, "No Item");
        Log.d("yy" , "yy " + json_order );
        return gson.fromJson(json_order, Order.class);
    }
}
