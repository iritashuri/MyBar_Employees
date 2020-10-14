package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        UpdateDeals_BTN_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeal();
            }
        });
    }

    private void openAddDeal() {
        Intent intent = new Intent(Activity_UpdateDeals.this, Activity_AddDeal.class);
        startActivity(intent);
    }

    private void findViews(){
        UpdateDeals_BTN_Add = findViewById(R.id.UpdateDeals_BTN_Add);
        UpdateDeals_PRBR_progressBar = findViewById(R.id.UpdateDeals_PRBR_progressBar);
    }
}