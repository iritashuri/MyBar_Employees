package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Activity_AddDeal extends AppCompatActivity {

    private EditText AddDeal_EDT_description;
    private EditText AddDeal_EDT_price;
    private Button AddDeal_BTN_Add;
    private ProgressBar AddDeal_PRBR_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_deal);

        findViews();
    }


    private void findViews(){
        AddDeal_EDT_description = findViewById(R.id.AddDeal_EDT_description);
        AddDeal_EDT_price = findViewById(R.id.AddDeal_EDT_price);
        AddDeal_BTN_Add = findViewById(R.id.AddDeal_BTN_Add);
        AddDeal_PRBR_progressBar = findViewById(R.id.AddDeal_PRBR_progressBar);
    }
}