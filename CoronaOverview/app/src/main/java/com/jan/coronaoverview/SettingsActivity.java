package com.jan.coronaoverview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences spref;
    private int spinnerCounter = 0;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Variable definition
        Spinner spinnerDarkMode = findViewById(R.id.spinnerDarkMode);
        spref = getSharedPreferences("values", 0);

        String[] arraySpinner = new String[] {"Systemeinstellung", "Dunkel", "Hell"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spinnerDarkMode.setAdapter(arrayAdapter);
        if(spref.getInt("DarkMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) == AppCompatDelegate.MODE_NIGHT_YES) {
            spinnerDarkMode.setSelection(1);
        } else if(spref.getInt("DarkMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) == AppCompatDelegate.MODE_NIGHT_NO) {
            spinnerDarkMode.setSelection(2);
        }

        spinnerDarkMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Prevent initial selection event on Activity start
                if(spinnerCounter == 0) {
                    spinnerCounter++;
                    return;
                }
                SharedPreferences.Editor editor = spref.edit();
                int mode = 0;
                if(id == 0) {
                    mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                } else if(id == 1) {
                    mode = AppCompatDelegate.MODE_NIGHT_YES;
                } else if(id == 2) {
                    mode = AppCompatDelegate.MODE_NIGHT_NO;
                }

                editor.putInt("DarkMode", mode);
                editor.apply();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get Attribute values
        TypedValue statusBarSettings = new TypedValue();
        TypedValue actionBarSettings = new TypedValue();
        getTheme().resolveAttribute(R.attr.statusBarSettings, statusBarSettings, true);
        getTheme().resolveAttribute(R.attr.actionBarSettings, actionBarSettings, true);
        int statusBarColor = ContextCompat.getColor(this, statusBarSettings.resourceId);
        int actionBarColor = ContextCompat.getColor(this, actionBarSettings.resourceId);
        //Set Status- and Actionbar Color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));
        getWindow().setStatusBarColor(statusBarColor);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}