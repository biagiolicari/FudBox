package com.andorid.fudbox.view.mainscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.andorid.fudbox.R;
import com.andorid.fudbox.utils.Constants;
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
        sharedLatLng.setLatLngMutableLiveData(latLng);

        // Configure navigation
        setupNavigation(latLng);
    }

    private LatLng getGeocodeDataFromIntent(Intent intent) {

        return new LatLng(
                intent.getDoubleExtra("lat", Constants.MILAN_DEFAULT_LAT),
                intent.getDoubleExtra("lng", Constants.MILAN_DEFAULT_LNG)
                        );
    }

    private void setupNavigation(LatLng latLng) {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_home_navHost);
        NavController navController = navHost.getNavController();
        // Set the navigation graph and pass arguments
        navController.setGraph(R.navigation.home_nav_graph);
        // Attach the BottomNavigationView to the NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}