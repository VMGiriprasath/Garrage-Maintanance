package com.example.team55app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.team55app.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    //Permission request code
    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //check permission
        if (checkPermission()) {

        } else {
            requestpermission();
        }

        //on click, Bill
        binding.makebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BillsActivity.class);
                startActivity(intent);
            }
        });

        //on click, Stock Availability
        binding.stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, StockActivity.class);
                startActivity(intent);
            }
        });


    }

    private void requestpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);

            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }
            }

    );

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}