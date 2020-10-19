package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_ItemCategory extends AppCompatActivity {

    RecyclerView UpdateDeals_LST_dealsList;
    private FirebaseFirestore db;

    private MySPV mySPV;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__item_category);

        findViews();

        // Set Firestore
        db = FirebaseFirestore.getInstance();

        // Set SP
        mySPV = new MySPV(this);

        showItems();
    }

    // Get current category to display from SP
    private String getCategory(){
        return mySPV.getString(MySPV.KEYS.CATEGORY, "No category");
    }

    // show list of the item from the cuurent category
    private void showItems() {
                // Get all items from Firestore
               db.collection("items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Make Array List of the items from the current category
                            ArrayList<Item> items = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("category").equals(getCategory())){
                                    Item temp = new Item(document.getString("category"),document.getString("description").toString(), document.getString("price"));
                                    items.add(temp);
                                }

                            }
                            // Display Items
                            Adapter_items adapter_items = new Adapter_items(Activity_ItemCategory.this, items);
                            adapter_items.setClickListeners(itemClickListener);
                            UpdateDeals_LST_dealsList.setLayoutManager(new LinearLayoutManager(Activity_ItemCategory.this));
                            UpdateDeals_LST_dealsList.setAdapter(adapter_items);
                        } else {
                            Log.w("pttt", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    // clicking Item action
    Adapter_items.lItemClickListener itemClickListener = new Adapter_items.lItemClickListener() {
        @Override
        public void itemClicked(Item item, int position) {

        }
    };

    private void findViews() {
        UpdateDeals_LST_dealsList = findViewById(R.id.UpdateDeals_LST_dealsList);
    }
}