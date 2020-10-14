package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Activity_Main extends AppCompatActivity {

    private EditText Main_EDT_password;
    private Button Main__BTN_addOrder;
    private Button Main_BTN_updateDeals;
    private TextView Main_TXT_newEmployee;

    private FirebaseFirestore db;

    private ArrayList<String> employees_passwords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        db = FirebaseFirestore.getInstance();

        getEmployees();


        Main__BTN_addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if password exist
                if(checkPassword()){
                    //openAddOrderToCustomer();
                }

            }
        });

        Main_TXT_newEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPassword()) {
                    openRegisterEmployee();
                }
            }
        });

        Main_BTN_updateDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if password exist or inserted
                if(checkPassword()) {
                    openUpdateDeals();
                }
            }
        });
    }

    private void findViews(){
        Main_EDT_password = findViewById(R.id.Main_EDT_password);
        Main__BTN_addOrder = findViewById(R.id.Main__BTN_addOrder);
        Main_BTN_updateDeals = findViewById(R.id.Main_BTN_updateDeals);
        Main_TXT_newEmployee = findViewById(R.id.Main_TXT_newEmployee);
    }

    // Open updateDeals activity
    private void openUpdateDeals(){
        Intent intent = new Intent(Activity_Main.this, Activity_UpdateDeals.class);
        startActivity(intent);
    }

    // Open RegisterEmployee activity
    private void openRegisterEmployee(){
        Intent intent = new Intent(Activity_Main.this, Activity_RegisterEmployee.class);
        startActivity(intent);
    }

    private boolean checkPassword(){
        final String password = Main_EDT_password.getText().toString().trim();
        if(password.length() == 0)
        {
            Main_EDT_password.setError("Password is required. ");
            return false;
        }
        for (String pass: employees_passwords){
            if(password.equals(pass)){
                return true;
            }
        }
        Main_EDT_password.setError("No employee with this password found. ");
        return false;
    }

    private void getEmployees(){
        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                employees_passwords.add(document.getString("password"));
                            }
                        } else {
                            Log.w("pttt", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}
