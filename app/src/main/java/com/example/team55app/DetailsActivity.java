package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.team55app.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    public static final String ADD_CUSTOMER_NAME_KEY = "customerName";
    public static final String ADD_CUSTOMER_PHONE_KEY = "phoneNumber";
    public static final String ADD_CUSTOMER_BIKE_REGNO = "bikeregno";
    public static final String ADD_CUSTOMER_BIKE_MAKE = "bikemake";
    public static final String ADD_CUSTOMER_BIKE_MODEL = "bikemodel";
    public static final String ADD_CUSTOMER_BIKE_KILOMETER = "bikekms";
    public static final String ADD_CUSTOMER_INSTAID = "bikeinstaid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public void addcustomer(View view) {
        String customerName = binding.customernameet.getText().toString();
        String customerobile = binding.mobilenumberet.getText().toString();
        String customerRegno = binding.bikeregnoet.getText().toString();
        String customerMake = binding.bikemake.getText().toString();
        String customerModel = binding.bikemodel.getText().toString();
        String customerKilo = binding.kilometers.getText().toString();
        String customerInsta = binding.instaid.getText().toString();

        if (customerName.length() == 0) {
            Toast.makeText(this, "Please Enter the Customer Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerobile.length() >= 0 && customerobile.length() < 10) {
            Toast.makeText(this, "Enter complete 10-Digit Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerRegno.length() == 0) {
            Toast.makeText(this, "Enter Complete Register No", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerMake.length() == 0) {
            Toast.makeText(this, "Enter Bike Make", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerModel.length() == 0) {
            Toast.makeText(this, "Enter Bike Model", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerKilo.length() == 0) {
            Toast.makeText(this, "Enter Bike Kilometer", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customerInsta.isEmpty()) {
            binding.instaid.setText("NA");
            return;
        } else {
            Intent intent = new Intent(this, ItemActivity.class);
            intent.putExtra(ADD_CUSTOMER_NAME_KEY, customerName);
            intent.putExtra(ADD_CUSTOMER_PHONE_KEY, customerobile);
            intent.putExtra(ADD_CUSTOMER_BIKE_REGNO, customerRegno);
            intent.putExtra(ADD_CUSTOMER_BIKE_MAKE, customerMake);
            intent.putExtra(ADD_CUSTOMER_BIKE_MODEL, customerModel);
            intent.putExtra(ADD_CUSTOMER_BIKE_KILOMETER, customerKilo);
            intent.putExtra(ADD_CUSTOMER_INSTAID, customerInsta);

            startActivity(intent);
            finish();
        }
    }
}