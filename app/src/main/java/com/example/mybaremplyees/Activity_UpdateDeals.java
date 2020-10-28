package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Activity_UpdateDeals extends AppCompatActivity {

    private Button UpdateDeals_BTN_Add;
    private Button UpdateDeals_BTN_back;
    private RecyclerView UpdateDeals_LST_dealsList;

    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    private ArrayList<Deal> deals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__update_deals);

        findViews();

        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("deals");

        deals = new ArrayList<>();

        showDeals();

        UpdateDeals_BTN_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeal();
            }
        });

        // Go back and close activity
        UpdateDeals_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void showDeals() {
        //Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Deal> myDeals = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Deal temp = new Deal(postSnapshot.child("description").getValue().toString(), postSnapshot.child("price").getValue().toString());
                    temp.setKey(postSnapshot.child("key").getValue().toString());
                    myDeals.add(temp);
                }
                Adapter_Deals adapter_deals = new Adapter_Deals(Activity_UpdateDeals.this, myDeals);
                adapter_deals.setClickListeners(dealItemClickListener);
                UpdateDeals_LST_dealsList.setLayoutManager(new LinearLayoutManager(Activity_UpdateDeals.this));
                UpdateDeals_LST_dealsList.setAdapter(adapter_deals);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("pttt", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    Adapter_Deals.DealItemClickListener dealItemClickListener = new Adapter_Deals.DealItemClickListener() {
        @Override
        public void itemClicked(Deal deal, int position) {
            openDealDialog(deal);
        }
    };

    private void openDealDialog(Deal deal) {
        DealDialog dealDialog = new DealDialog(deal);
        dealDialog.show(getSupportFragmentManager(), "Update Deal");
    }

    private void openAddDeal() {
        Intent intent = new Intent(Activity_UpdateDeals.this, Activity_AddDeal.class);
        startActivity(intent);
    }

    private void findViews() {
        UpdateDeals_BTN_Add = findViewById(R.id.UpdateDeals_BTN_Add);
        UpdateDeals_LST_dealsList = findViewById(R.id.UpdateDeals_LST_dealsList);
        UpdateDeals_BTN_back = findViewById(R.id.UpdateDeals_BTN_back);
    }


}