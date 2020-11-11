package com.jan.coronaoverview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.android.material.color.MaterialColors;

public class SettingsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
}