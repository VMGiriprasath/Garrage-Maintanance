package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.team55app.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private static int MILLISEC = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Disable Night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

            }
        }, MILLISEC);


    }
}