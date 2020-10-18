package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Activity_ItemCategory extends AppCompatActivity {

    RecyclerView UpdateDeals_LST_dealsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__item_category);

        findViews();
    }

    private void findViews() {
        UpdateDeals_LST_dealsList = findViewById(R.id.UpdateDeals_LST_dealsList);
    }
}