package com.example.mybaremplyees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Activity_RegisterEmployee extends AppCompatActivity {

    private EditText RegEmployee_EDT_userName;
    private EditText RegEmployee_EDT_password;
    private Button RegEmployee_BTN_Register;
    private ProgressBar RegEmployee_PRBR_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register_employee);

        findViews();
    }


    private void findViews(){
        RegEmployee_EDT_userName = findViewById(R.id.RegEmployee_EDT_userName);
        RegEmployee_EDT_password = findViewById(R.id.RegEmployee_EDT_password);
        RegEmployee_BTN_Register = findViewById(R.id.RegEmployee_BTN_Register);
        RegEmployee_PRBR_progressBar = findViewById(R.id.RegEmployee_PRBR_progressBar);
    }
}