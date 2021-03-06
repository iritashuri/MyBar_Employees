package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

public class Activity_ViewOrder extends AppCompatActivity {

    private RecyclerView Order_LST_dealsList;
    private Button Order_BTN_back;

    private MySPV mySPV;
    private Gson json = new Gson();

    private Order current_order = new Order();

    private static Activity_ViewOrder activity_viewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        activity_viewOrder = this;

        findViews();

        // Set SP
        mySPV = new MySPV(this);
        Gson json = new Gson();

        //get current order to display
        getCurrentOrderFromSP();

        showItems();

        // Go back and close Activity
        Order_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getCurrentOrderFromSP() {
        String json_order = mySPV.getString(MySPV.KEYS.CURRENT_ORDER, "no order");
        current_order = json.fromJson(json_order,Order.class);
    }

    private void showItems() {
        // Display Items
        Adapter_Items adapter_items = new Adapter_Items(Activity_ViewOrder.this, current_order.getItem_list());
        adapter_items.setClickListeners(itemClickListener);
        Order_LST_dealsList.setLayoutManager(new LinearLayoutManager(Activity_ViewOrder.this));
        Order_LST_dealsList.setAdapter(adapter_items);
    }

    private void findViews() {
        Order_LST_dealsList = findViewById(R.id.Order_LST_dealsList);
        Order_BTN_back = findViewById(R.id.Order_BTN_back);
    }

    // clicking Item action
    Adapter_Items.lItemClickListener itemClickListener = new Adapter_Items.lItemClickListener() {
        @Override
        public void itemClicked(Item item, int position) {
            //Open dialog
            openDeleteItemDialog(item);
        }
    };

    private void openDeleteItemDialog(Item item) {
        ItemDeleteDialog itemDialog = new ItemDeleteDialog(item);
        itemDialog.show(getSupportFragmentManager(), "delete item");
    }

    public static Activity_ViewOrder getInstance(){
        return activity_viewOrder;
    }

    public void start(){
        Intent intent = new Intent(Activity_ViewOrder.this, Activity_ViewOrder.class);
        startActivity(intent);
    }
}