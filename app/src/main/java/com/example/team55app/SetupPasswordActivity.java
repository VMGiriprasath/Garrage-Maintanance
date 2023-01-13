package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.team55app.databinding.ActivitySetupPasswordBinding;

public class SetupPasswordActivity extends AppCompatActivity {

    private ActivitySetupPasswordBinding binding;
    public static final String SETUP_GARRAGE_NAME_KEY = "setup-garrage-name-key";
    public static final String SETUP_GARRAGE_ADDRESS_KEY = "setup-garrage-address-key";
    public static final String SETUP_GARRAGE_CONTACT_KEY = "setup-garrage-contact-key";
    public static final String SETUP_PASSWORD_KEY = "setup-password-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //onclick setup button
        binding.setupPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GarrageNameValue = binding.setupGarragenameValue.getText().toString();
                String GarrageAddressValue = binding.setupGarrageAddressValue.getText().toString();
                String GarrageContactValue = binding.setupGarrageContactValue.getText().toString();
                String newPassword = binding.setupNewPasswordValue.getText().toString();
                String confirmPassword = binding.setupConfirmPasswordValue.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SetupPasswordActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SETUP_GARRAGE_NAME_KEY, GarrageNameValue);
                    editor.putString(SETUP_GARRAGE_ADDRESS_KEY, GarrageAddressValue);
                    editor.putString(SETUP_GARRAGE_CONTACT_KEY, GarrageContactValue);
                    editor.putString(SETUP_PASSWORD_KEY, newPassword);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Password Setup Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetupPasswordActivity.this, BillsActivity.class));
                    finish();
                } else {
                    Toast.makeText(SetupPasswordActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}