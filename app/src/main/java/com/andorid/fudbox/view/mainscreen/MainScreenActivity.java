package com.andorid.fudbox.view.mainscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.andorid.fudbox.R;
import com.andorid.fudbox.viewmodel.mainscreen.shared.SharedLatLng;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {
    private SharedLatLng sharedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        sharedLatLng = new ViewModelProvider(this).get(SharedLatLng.class);

        // Get latitude and longitude from intent
        Intent intent = getIntent();
        LatLng latLng = getGeocodeDataFromIntent(intent);

        // Configure navigation
        setupNavigation(latLng);
    }

    private LatLng getGeocodeDataFromIntent(Intent intent) {
        return new LatLng(
                Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lat"))),
                Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lng")))
        );
    }

    private void setupNavigation(LatLng latLng) {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_home_navHost);
        NavController navController = navHost.getNavController();
        sharedLatLng.setLatLngMutableLiveData(latLng);
        // Set the navigation graph and pass arguments
        navController.setGraph(R.navigation.home_nav_graph);
        // Attach the BottomNavigationView to the NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}