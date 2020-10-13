package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Activity_Main extends AppCompatActivity {

    private EditText Main_EDT_password;
    private Button Main__BTN_addOrder;
    private Button Main_BTN_updateDeals;
    private TextView Main_TXT_newEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    private void findViews(){
        Main_EDT_password = findViewById(R.id.Main_EDT_password);
        Main__BTN_addOrder = findViewById(R.id.Main__BTN_addOrder);
        Main_BTN_updateDeals = findViewById(R.id.Main_BTN_updateDeals);
        Main_TXT_newEmployee = findViewById(R.id.Main_TXT_newEmployee);
    }
}