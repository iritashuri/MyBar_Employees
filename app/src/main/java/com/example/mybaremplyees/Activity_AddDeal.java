package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_AddDeal extends AppCompatActivity {

    private EditText AddDeal_EDT_description;
    private EditText AddDeal_EDT_price;
    private Button AddDeal_BTN_Add;
    private ProgressBar AddDeal_PRBR_progressBar;
    private  Button AddDeal_BTN_back;

    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_deal);

        findViews();

        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("deals/");

        AddDeal_BTN_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDeal();
            }
        });

        // Go back and close activity
        AddDeal_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews(){
        AddDeal_EDT_description = findViewById(R.id.AddDeal_EDT_description);
        AddDeal_EDT_price = findViewById(R.id.AddDeal_EDT_price);
        AddDeal_BTN_Add = findViewById(R.id.AddDeal_BTN_Add);
        AddDeal_PRBR_progressBar = findViewById(R.id.AddDeal_PRBR_progressBar);
        AddDeal_BTN_back = findViewById(R.id.AddDeal_BTN_back);
    }

    // Insert new deal to database
    private void AddDeal(){
        // Get data
        final String description = AddDeal_EDT_description.getText().toString().trim();
        final String price = AddDeal_EDT_price.getText().toString().trim();

        // Check if there are errors and return if there is
        if(checkErrors(description, price)){
            return;
        }

        // Show progress bar
        AddDeal_PRBR_progressBar.setVisibility(View.VISIBLE);

        // InsertDealDetails
        insertDetails(description, price);
    }

    // Check if there are errors and return true if there is
    boolean checkErrors(String description, String price){

        // Check if there is no empty value
        if(checkEmpties(description, price)){
            return true;
        }

        // Validate price
        if(!isValidNumber(price)){
            AddDeal_EDT_price.setError("Please enter valid price");
            return true;
        }

        return false;
    }

    private boolean isValidNumber(String price) {
        if (Integer.parseInt(price) > 0)
            return true;
        return false;
    }

    // Check if email or password was not insert
    private boolean checkEmpties(String description, String price){
        boolean to_return = false;

        if (checkEmptyTxt(description, AddDeal_EDT_description, "Description")){
            to_return = true;
        }
        if (checkEmptyTxt(price, AddDeal_EDT_price, "Price")){
            to_return = true;
        }
        return to_return;
    }


    // Check if edit text is empty
    private boolean checkEmptyTxt(String str, EditText editText, String missing){
        if(TextUtils.isEmpty(str)){
            editText.setError(missing + "is Required. ");
            return true;
        }
        return false;
    }


    private void insertDetails(String description, String price){

        // Create a new deal with a description and price
        Map<String, Object> deal = new HashMap<>();
        deal.put("description", description);
        deal.put("price", price);

        //realTime
        Deal d = new Deal(description, price);
        //myRef.child(d.getKey()).setValue(d);

        // Add new deal to realTime database
        myRef.push().setValue(d);

        // Add a new document with a generated ID firestore
        db.collection("deals")
                .add(deal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Show success toast message
                        Toast.makeText(Activity_AddDeal.this, "Deal inserted successfully", Toast.LENGTH_LONG).show();
                        // Close activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show fail toast message
                        Toast.makeText(Activity_AddDeal.this, "Error adding employee", Toast.LENGTH_LONG).show();
                        Log.w("pttt", "Error adding document", e);
                        // Stop showing progress bar
                        AddDeal_PRBR_progressBar.setVisibility(View.GONE);
                    }
                });
    }
}