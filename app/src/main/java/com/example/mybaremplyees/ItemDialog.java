package com.example.mybaremplyees;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ItemDialog extends AppCompatDialogFragment {
    private ElegantNumberButton Dialog_Item_NBTM_number;
    private FirebaseFirestore db;

    private MySPV mySPV;
    private Gson gson = new Gson();

    private Item item;


    public ItemDialog() {
    }

    public ItemDialog(Item item) {
        this.item = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_item, null);

        Dialog_Item_NBTM_number = view.findViewById(R.id.Dialog_Item_NBTM_number);

        //item = getCurrentItemFromSP();


        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close dialog
            }
        }).setPositiveButton("update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mySPV = new MySPV(getActivity().getApplicationContext());
                final String num_of_items_str = Dialog_Item_NBTM_number.getNumber();
                final int num_of_items = Integer.parseInt(num_of_items_str);
                // Inset item to current order on SP num_of_items times
                for(int n = 0; n < num_of_items; n++){
                    insetNewOrderToSP(item);
                }
            }
        });
        return builder.create();
    }

    private void insetNewOrderToSP(Item item) {
        // Get order from SP
        Order new_order = getCurrentOrderFromSP();
        // Add new item
        new_order.addOneItemToList(item);
        new_order.setTotal_price();
        // Insert to SP
        String json_order = gson.toJson(new_order);
        mySPV.putString(MySPV.KEYS.CURRENT_ORDER, json_order);
    }

    private Item getCurrentItemFromSP() {
        String json_item = mySPV.getString(MySPV.KEYS.CURRENT_ITEM, "No Item");
        return gson.fromJson(json_item, Item.class);
    }

    private Order getCurrentOrderFromSP(){
        String json_order = mySPV.getString(MySPV.KEYS.CURRENT_ORDER, "No Item");
        Log.d("yy" , "yy " + json_order );
        return gson.fromJson(json_order, Order.class);
    }
}
