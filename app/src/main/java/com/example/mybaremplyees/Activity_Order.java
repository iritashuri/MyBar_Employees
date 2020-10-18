package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;

public class Activity_Order extends AppCompatActivity {

    // Set Buttons
    private Button Order_BTN_cocktails;
    private Button Order_BTN_Wines;
    private Button Order_BTN_Daily_Beers;
    private Button Order_BTN_Chasers;
    private Button Order_BTN_Daily_SoftDrinks;
    private Button Order_BTN_Food;
    private Button Order_BTN_Daily_Deals;
    private Button Order_BTN_Daily_Add;
    private Button Order_BTN_Daily_View;
    private Button Order_BTN_Daily_Delete;

    // Set SP
    private MySPV mySPV;
    Gson gson = new Gson();

    //set new Order
    Order current_order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__order);

        finViews();

        // Set SP
        mySPV = new MySPV(this);

        // Set new empty order on SP
        setOrderOnSP();

    }

    private void setOrderOnSP() {
        String json = gson.toJson(current_order);
        mySPV.putString(MySPV.KEYS.CURRENT_ORDER, json);
    }

    private void finViews() {
        Order_BTN_cocktails = findViewById(R.id.Order_BTN_cocktails);
        Order_BTN_Wines = findViewById(R.id.Order_BTN_Wines);
        Order_BTN_Daily_Beers = findViewById(R.id.Order_BTN_Daily_Beers);
        Order_BTN_Chasers = findViewById(R.id.Order_BTN_Chasers);
        Order_BTN_Daily_SoftDrinks = findViewById(R.id.Order_BTN_Daily_SoftDrinks);
        Order_BTN_Food = findViewById(R.id.Order_BTN_Food);
        Order_BTN_Daily_Deals = findViewById(R.id.Order_BTN_Daily_Deals);
        Order_BTN_Daily_Add = findViewById(R.id.Order_BTN_Daily_Add);
        Order_BTN_Daily_View = findViewById(R.id.Order_BTN_Daily_View);
        Order_BTN_Daily_Delete = findViewById(R.id.Order_BTN_Daily_Delete);
    }
}