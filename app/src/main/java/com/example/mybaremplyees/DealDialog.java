package com.example.mybaremplyees;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DealDialog extends AppCompatDialogFragment {
    private FirebaseDatabase database;
    private DatabaseReference ref;

    private Deal deal;

    public DealDialog() {
    }

    public DealDialog(Deal deal) {
        this.deal = deal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_deal, null);

        final EditText UpdateDeal_EDT_description = view.findViewById(R.id.UpdateDeal_EDT_description);
        final EditText UpdateDeal_EDT_price = view.findViewById(R.id.UpdateDeal_EDT_price);
        UpdateDeal_EDT_description.setText(deal.getDescription());
        UpdateDeal_EDT_price.setText(deal.getPrice());

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("deals");

        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close dialog
            }
        }).setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                // get new description and price from the user
                final String new_description = UpdateDeal_EDT_description.getText().toString().trim();
                final String new_price = UpdateDeal_EDT_price.getText().toString().trim();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Go ove all deals and find current deal
                        for (DataSnapshot current_deal: snapshot.getChildren()) {
                            // if found
                            if (deal.getKey().equals(current_deal.child("key").getValue().toString().trim())){
                                // Create HashMap with new value
                                Map<String, String> new_deal_map = new HashMap<>();
                                new_deal_map.put("description", new_description);
                                new_deal_map.put("key", deal.getKey());
                                new_deal_map.put("price", new_price);
                                // Set new values
                                ref.child(current_deal.getKey()).setValue(new_deal_map);
                                break;
                            }
                        }
                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).setNeutralButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot current_deal: snapshot.getChildren()) {
                            Log.d("ptt", "desl: " + ref.child(current_deal.getKey()));
                            //Log.d("ptt", "des2: " + deal.getKey());
                            //this is all you need to get a specific user by Uid
                            if (deal.getKey().equals(current_deal.child("key").getValue().toString().trim())){
                                ref.child(current_deal.getKey()).removeValue();
                                break;
                            }
                        }
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return builder.create();
    }
}
