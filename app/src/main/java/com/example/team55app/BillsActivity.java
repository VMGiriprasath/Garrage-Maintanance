package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.team55app.databinding.ActivityBillsBinding;

public class BillsActivity extends AppCompatActivity {
    private ActivityBillsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //check setup password
        checkPasswordSetup();
        //on click fab, Detail Activity
        binding.makebillsfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillsActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkPasswordSetup() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString(SetupPasswordActivity.SETUP_PASSWORD_KEY, null) == null) {
            Intent intent = new Intent(this, SetupPasswordActivity.class);
            startActivity(intent);
            finish();
        }
    }
}