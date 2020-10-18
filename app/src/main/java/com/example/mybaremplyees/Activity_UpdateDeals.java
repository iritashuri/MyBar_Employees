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
    private ProgressBar UpdateDeals_PRBR_progressBar;
    private RecyclerView UpdateDeals_LST_dealsList;

    private FirebaseFirestore db;
    private FirebaseDatabase database;
    DatabaseReference myRef;


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

        UpdateDeals_BTN_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeal();
            }
        });

        showDeals();
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
        // Get deals from the firestore

//        db.collection("deals")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            ArrayList<Deal> myDeals = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Deal temp = new Deal(document.getString("description"), document.getString("price"));
//                                myDeals.add(temp);
//                            }
//                            Adapter_Deals adapter_deals = new Adapter_Deals(Activity_UpdateDeals.this, myDeals);
//                            adapter_deals.setClickListeners(dealItemClickListener);
//                            UpdateDeals_LST_dealsList.setLayoutManager(new LinearLayoutManager(Activity_UpdateDeals.this));
//                            UpdateDeals_LST_dealsList.setAdapter(adapter_deals);
//                        } else {
//                            Log.w("pttt", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }

    Adapter_Deals.DealItemClickListener dealItemClickListener = new Adapter_Deals.DealItemClickListener() {
        @Override
        public void itemClicked(Deal deal, int position) {
            Toast.makeText(Activity_UpdateDeals.this, deal.getDescription() + " Clicked", Toast.LENGTH_LONG).show();
            openDealDialog(deal);
        }
    };

    private void openDealDialog(Deal deal) {
        DealDialog dealDialog = new DealDialog(deal);
        dealDialog.show(getSupportFragmentManager(), "Update Deal");
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_UpdateDeals.this);
//        View mView = getLayoutInflater().inflate(R.layout.dialog_deal, null);
//        EditText UpdateDeal_EDT_description = mView.findViewById(R.id.UpdateDeal_EDT_description);
//        EditText UpdateDeal_EDT_price = mView.findViewById(R.id.UpdateDeal_EDT_price);
//        Button UpdateDeal_BTN_Update = mView.findViewById(R.id.UpdateDeal_BTN_Update);
//        Button UpdateDeal_BTN_Delete = mView.findViewById(R.id.UpdateDeal_BTN_Delete);

    }

    private void openAddDeal() {
        Intent intent = new Intent(Activity_UpdateDeals.this, Activity_AddDeal.class);
        startActivity(intent);
    }

    private void findViews() {
        UpdateDeals_BTN_Add = findViewById(R.id.UpdateDeals_BTN_Add);
        UpdateDeals_PRBR_progressBar = findViewById(R.id.UpdateDeals_PRBR_progressBar);
        UpdateDeals_LST_dealsList = findViewById(R.id.UpdateDeals_LST_dealsList);
    }


}