package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_RegisterEmployee extends AppCompatActivity {

    private EditText RegEmployee_EDT_userName;
    private EditText RegEmployee_EDT_password;
    private Button RegEmployee_BTN_Register;
    private Button RegEmployee_BTN_back;
    private ProgressBar RegEmployee_PRBR_progressBar;


    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register_employee);

        findViews();

        db = FirebaseFirestore.getInstance();

        RegEmployee_BTN_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterEmployee();
            }
        });

        // Go back and close activity
        RegEmployee_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findViews(){
        RegEmployee_EDT_userName = findViewById(R.id.RegEmployee_EDT_userName);
        RegEmployee_EDT_password = findViewById(R.id.RegEmployee_EDT_password);
        RegEmployee_BTN_Register = findViewById(R.id.RegEmployee_BTN_Register);
        RegEmployee_PRBR_progressBar = findViewById(R.id.RegEmployee_PRBR_progressBar);
        RegEmployee_BTN_back = findViewById(R.id.RegEmployee_BTN_back);
    }
    // Insert new employee details
    private void RegisterEmployee(){
        // Get data
        final String user_name = RegEmployee_EDT_userName.getText().toString().trim();
        final String password = RegEmployee_EDT_password.getText().toString().trim();

        // Check if there are errors and return if there is
        if(checkErrors(user_name, password)){
            return;
        }

        // Show progress bar
        RegEmployee_PRBR_progressBar.setVisibility(View.VISIBLE);

        // Register the employee in firebase
        insertDetails(user_name, password);

    }

    // Check if there are errors and return true if there is
    boolean checkErrors(String user_name, String password){

        // Check if there is no empty value
        if(checkEmpties(user_name, password)){
            return true;
        }

        // Validate password length
        if(password.length() < 4){
            RegEmployee_EDT_password.setError("Password Must include at least 4 characters. ");
            return true;
        }

        return false;
    }

    // Check if email or password was not insert
    private boolean checkEmpties(String user_name, String password){
        boolean to_return = false;

        if (checkEmptyTxt(user_name, RegEmployee_EDT_userName, "User name")){
            to_return = true;
        }
        if (checkEmptyTxt(password, RegEmployee_EDT_password, "Password")){
            to_return = true;
        }
        return to_return;
    }


    // Check if edit text is empty
    private boolean checkEmptyTxt(String str, EditText editText, String missing){
        if(TextUtils.isEmpty(str)){
            editText.setError(missing + "is Required. ");
            return true;
        }
        return false;
    }

    private void insertDetails(String user_name, String password){
        // Create a new user with a first, middle, and last name
        Map<String, Object> employee = new HashMap<>();
        employee.put("user_name", user_name);
        employee.put("password", password);

        // Add a new document with a generated ID
        db.collection("employees")
                .add(employee)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Show success toast message
                        Toast.makeText(Activity_RegisterEmployee.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        // Close activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show success toast message
                        Toast.makeText(Activity_RegisterEmployee.this, "Error adding employee", Toast.LENGTH_LONG).show();
                        Log.w("pttt", "Error adding document", e);
                        // Stop showing progress bar
                        RegEmployee_PRBR_progressBar.setVisibility(View.GONE);
                    }
                });
    }
}