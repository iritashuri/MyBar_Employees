package com.example.mybaremplyees;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FreeDrinksDialog extends AppCompatDialogFragment {
    // Set FireStore
    private FirebaseFirestore db;

    private Context context;
    // Set userId
    String userId = "";

    String toastMessage = "";

    EditText UpdateDeal_EDT_Email;
    TextView UpdateDeal_EDT_message;

    public FreeDrinksDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_freedrinks, null);

        UpdateDeal_EDT_Email = view.findViewById(R.id.UpdateDeal_EDT_Email);
        UpdateDeal_EDT_message = view.findViewById(R.id.UpdateDeal_EDT_message);

        context = getActivity();

        // Set Firebase
        db = FirebaseFirestore.getInstance();


        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close dialog
            }
        }).setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                // Get customer email
                final String email = UpdateDeal_EDT_Email.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(context, "Please enter customer email", Toast.LENGTH_LONG).show();
                } else {
                    // Get user id from Firestore according to his email address
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                toastMessage = "User was not found";
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.get("email").equals(email)) {

                                        userId = document.getId();
                                        // Set new drinks number
                                        int drinks;
                                        if (!(document.get("drinks").toString().isEmpty()))
                                            drinks = Integer.parseInt(document.get("drinks").toString());
                                        else
                                            drinks = 0;

                                        String name = document.get("full_name").toString();
                                        String email = document.get("email").toString();
                                        String phone = document.get("phone").toString();

                                        toastMessage = setDrinks(drinks, name, email, phone);
                                        break;
                                    }
                                }
                                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            }
        });
        return builder.create();
    }

    private String setDrinks(int drinks, String name, String email, String phone) {
        DocumentReference documentReference = db.collection("users").document(userId);

        // Test number of drinks for the customer
        if(drinks >= 10) {
            drinks -= 10;
            Log.d("drink", "" + drinks);
            Map<String, Object> user = new HashMap<>();
            user.put("full_name", name);
            user.put("email", email);
            user.put("phone", phone);
            user.put("drinks", drinks);

            documentReference.set(user);
            return "Updated successfully";
        }
        else {
            // There is not enough drinks saved for the customer in order to get free drink
            return "The customer has " + drinks + " drinks counted on this card, " + (10-drinks) + " left";
        }

    }
}
