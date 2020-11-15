package com.jan.coronaoverview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private int colorActionBar;
    private int colorBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        SharedPreferences spref = getSharedPreferences("values", 0);

        //First Run Configuration
        if(!spref.getBoolean("firstrun", false)) {
            SharedPreferences.Editor editor = spref.edit();
            editor.putInt("DarkMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            editor.putBoolean("firstrun", true);
            editor.commit();
        }

        //Set Dark-Mode Setting
        AppCompatDelegate.setDefaultNightMode(spref.getInt("DarkMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));

        //Start on first entry
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RLPFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.item1);
        getSupportActionBar().setSubtitle("Rheinland-Pfalz");

        //Variables for color-changing
        colorActionBar = getColor(R.color.pink_500);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorActionBar));
        colorBottom = getColor(R.color.pink_500);
        final int duration = 350;

        //Navigation Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch(item.getItemId()) {
                    case R.id.item1:
                        ObjectAnimator animation = ObjectAnimator.ofArgb(bottomNavigationView, "backgroundColor", colorBottom, getColor(R.color.pink_500));
                        animation.setEvaluator(new ArgbEvaluator());
                        animation.setDuration(duration);
                        animation.start();
                        colorBottom = getColor(R.color.pink_500);
                        //ActionBar Animation
                        ColorDrawable[] transitionColors = {new ColorDrawable(colorActionBar), new ColorDrawable(getColor(R.color.pink_500))};
                        TransitionDrawable transitionDrawable = new TransitionDrawable(transitionColors);
                        getSupportActionBar().setBackgroundDrawable(transitionDrawable);
                        transitionDrawable.startTransition(duration);
                        colorActionBar = getColor(R.color.pink_500);
                        //StatusBar Color
                        ValueAnimator valueAnimator = ValueAnimator.ofArgb(getWindow().getStatusBarColor(), getColor(R.color.pink_800));
                        valueAnimator.setDuration(duration);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int)animation.getAnimatedValue();
                                getWindow().setStatusBarColor(value);
                            }
                        });
                        valueAnimator.start();

                        selected = new RLPFragment();
                        getSupportActionBar().setSubtitle("Rheinland-Pfalz");
                        break;
                    case R.id.itemMyk:
                        ObjectAnimator animation3 = ObjectAnimator.ofArgb(bottomNavigationView, "backgroundColor", colorBottom, getColor(R.color.deeporange_500));
                        animation3.setEvaluator(new ArgbEvaluator());
                        animation3.setDuration(duration);
                        animation3.start();
                        colorBottom = getColor(R.color.deeporange_500);
                        //ActionBar Animation
                        ColorDrawable[] transitionColors3 = {new ColorDrawable(colorActionBar), new ColorDrawable(getColor(R.color.deeporange_500))};
                        TransitionDrawable transitionDrawable3 = new TransitionDrawable(transitionColors3);
                        getSupportActionBar().setBackgroundDrawable(transitionDrawable3);
                        transitionDrawable3.startTransition(duration);
                        colorActionBar = getColor(R.color.deeporange_500);
                        //StatusBar Color
                        ValueAnimator valueAnimator3 = ValueAnimator.ofArgb(getWindow().getStatusBarColor(), getColor(R.color.deeporange_500_dark));
                        valueAnimator3.setDuration(duration);
                        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int)animation.getAnimatedValue();
                                getWindow().setStatusBarColor(value);
                            }
                        });
                        valueAnimator3.start();

                        selected = new MykFragment();
                        getSupportActionBar().setSubtitle("Mayen-Koblenz");
                        break;
                    case R.id.item2:
                        ObjectAnimator animation2 = ObjectAnimator.ofArgb(bottomNavigationView, "backgroundColor", colorBottom, getColor(R.color.design_default_color_secondary));
                        animation2.setEvaluator(new ArgbEvaluator());
                        animation2.setDuration(duration);
                        animation2.start();
                        colorBottom = getColor(R.color.design_default_color_secondary);
                        //ActionBar Animation
                        ColorDrawable[] transitionColors2 = {new ColorDrawable(colorActionBar), new ColorDrawable(getColor(R.color.design_default_color_secondary))};
                        TransitionDrawable transitionDrawable2 = new TransitionDrawable(transitionColors2);
                        getSupportActionBar().setBackgroundDrawable(transitionDrawable2);
                        transitionDrawable2.startTransition(duration);
                        colorActionBar = getColor(R.color.design_default_color_secondary);
                        //StatusBar Color
                        ValueAnimator valueAnimator2 = ValueAnimator.ofArgb(getWindow().getStatusBarColor(), getColor(R.color.design_default_color_secondary_variant));
                        valueAnimator2.setDuration(duration);
                        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int)animation.getAnimatedValue();
                                getWindow().setStatusBarColor(value);
                            }
                        });
                        valueAnimator2.start();

                        selected = new CountryFragment();
                        getSupportActionBar().setSubtitle("Deutschland");
                        break;
                }

                bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {

                    }
                });

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, selected).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.itemSettings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}