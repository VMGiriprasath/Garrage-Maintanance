package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.team55app.databinding.ActivityStockBinding;

public class StockActivity extends AppCompatActivity {
    private ActivityStockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //navigation
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}