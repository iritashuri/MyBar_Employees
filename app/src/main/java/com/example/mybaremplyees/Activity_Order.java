package com.example.mybaremplyees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_Order extends AppCompatActivity implements LocationListener{

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=1340+3;

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
    private EditText Order_EDT_customerMail;

    // Set FireStore
    private FirebaseFirestore db;

    // Set SP
    private MySPV mySPV;
    Gson gson = new Gson();

    // Set new Order
    Order current_order = new Order();

    // Set userId
    String userId = "";

    // Set location
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__order);

        finViews();

        // Set SP
        mySPV = new MySPV(this);

        // Set Firebase
        db = FirebaseFirestore.getInstance();

        // Set new empty order on SP
        setOrderOnSP();

        // Set location
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);


        // Open cocktail list
        Order_BTN_cocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.COCKTAILS);
            }
        });

        // Open wines list
        Order_BTN_Wines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.WINES);
            }
        });

        // Open beers list
        Order_BTN_Daily_Beers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.BEERS);
            }
        });

        // Open chasers list
        Order_BTN_Chasers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.CHASERS);
            }
        });

        // Open soft drinks list
        Order_BTN_Daily_SoftDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.SOFT_DRINKS);
            }
        });

        // Open soft food list
        Order_BTN_Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemCategory(Item.CATEGORIES.FOOD);
            }
        });

        // Open deals list
        Order_BTN_Daily_Deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDealsView();
            }
        });

        // see current order
        Order_BTN_Daily_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderView();
            }
        });

        Order_BTN_Daily_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentOrderFromSP();
                getPermission(new Permission_CallBack() {
                    @Override
                    public void onComplete() {
                        addOrderToCustomer();
                    }
                });

            }
        });
    }

    private void getPermission(final Permission_CallBack permission_callBack){
        // Set location
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        if(permission_callBack != null){
            permission_callBack.onComplete();
        }
    }
    private void addOrderToCustomer() {
        // Get customer mail
        final String customer_mail = Order_EDT_customerMail.getText().toString().trim();
        // Validate that mail inserted
        if(customer_mail.isEmpty()){
            Toast.makeText(Activity_Order.this, "Please enter customer email address", Toast.LENGTH_LONG).show();
            return;
        }
        // Get user id from Firestore according to his email address
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.get("email").equals(customer_mail)){

                            userId = document.getId();
                            // Set user id on current user
                            current_order.setUser_id(userId);
                            // get Orders collection
                            CollectionReference collectionReference  = db.collection("orders");

                            // Set timestamp
                            setOrderTimeStamp();

                            // Set new drinks number
                            int drinks;
                            if(!(document.get("drinks").toString().isEmpty()))
                                 drinks = Integer.parseInt(document.get("drinks").toString());
                            else
                                drinks = 0;

                            String name = document.get("full_name").toString();
                            String email = document.get("email").toString();
                            String phone = document.get("phone").toString();

                            setDrinks(drinks,name, email, phone);

                            // Make map to insert details to order document
                            Map<String,Object> order = new HashMap<>();
                            order.put("user_id", current_order.getUser_id());
                            order.put("item_list", current_order.getItem_list());
                            order.put("total_price", current_order.getTotal_price());
                            order.put("timestamp", current_order.getTimestamp());
                            order.put("lat", current_order.getLat());
                            order.put("lon", current_order.getLon());

                            // Add new order document to orders collection
                            collectionReference.add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Activity_Order.this, "Added order successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Activity_Order.this, "User not found", Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void setDrinks(int drinks, String name, String email, String phone) {
        DocumentReference documentReference = db.collection("users").document(userId);

        for(Item it: current_order.getItem_list()){
            if(it.isAlcoholGlass())
                drinks++;
        }
        Log.d("drink",  ""+ drinks);
        Map<String,Object> user = new HashMap<>();
        user.put("full_name" , name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("drinks", drinks);

        documentReference.set(user);

    }


    // Get current order from SP and set in on current_order
    private void getCurrentOrderFromSP() {
            String json_order = mySPV.getString(MySPV.KEYS.CURRENT_ORDER, "No Item");
            current_order = gson.fromJson(json_order, Order.class);
    }

    private void openOrderView() {
        Intent intent = new Intent(Activity_Order.this, Activity_ViewOrder.class);
        startActivity(intent);
    }

    private void openDealsView() {
        Intent intent = new Intent(Activity_Order.this, Activity_DealsDisplay.class);
        startActivity(intent);
    }

    private void openItemCategory(String category) {
        // Set category on SP
        mySPV.putString(MySPV.KEYS.CATEGORY, category);

        // open ItemCategory Activity
        Intent intent = new Intent(Activity_Order.this, Activity_ItemCategory.class);
        startActivity(intent);

    }

    // save order on SP
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
        Order_EDT_customerMail = findViewById(R.id.Order_EDT_customerMail);
    }

    void setOrderLocation(){
        current_order.setLat(location.getLatitude());
        current_order.setLon(location.getLongitude());
        Log.d("locationnn", ""+ current_order.getLat() + " " + current_order.getLon());

    }
    void setOrderTimeStamp(){
        current_order.setTimestamp(java.text.DateFormat.getDateTimeInstance().format(new Date()));
    }

    private void setLocation() {

        // Check map permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please permit location accsess", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }catch (Exception e){
            Log.d("locationnn", ""+ e);
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        current_order.setLat(location.getLatitude());
        current_order.setLon(location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check map permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please permit location accsess", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            setLocation();
            setOrderLocation();

        }catch (Exception e){
            Log.d("locationnn", ""+ e);
            e.printStackTrace();
        }

    }

}