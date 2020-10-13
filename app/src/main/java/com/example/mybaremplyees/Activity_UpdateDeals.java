package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

public class Activity_UpdateDeals extends AppCompatActivity {

    private Button UpdateDeals_BTN_Add;
    private ProgressBar UpdateDeals_PRBR_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__update_deals);

        findViews();
    }

    private void findViews(){
        UpdateDeals_BTN_Add = findViewById(R.id.UpdateDeals_BTN_Add);
        UpdateDeals_PRBR_progressBar = findViewById(R.id.UpdateDeals_PRBR_progressBar);
    }
}