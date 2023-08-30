package com.andorid.fudbox.view.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.andorid.fudbox.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_home_navHost);
        NavController navController = navHost.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        Intent intent = getIntent();
        LatLng latLng = getGeocodeDataFromIntent(intent);
    }

    private LatLng getGeocodeDataFromIntent(Intent intent){
        return new LatLng(
               Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lat"))),
                Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lng"))));
    }
}