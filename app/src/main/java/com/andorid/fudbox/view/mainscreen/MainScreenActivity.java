package com.andorid.fudbox.view.mainscreen;

import android.app.FragmentManager;
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

        Intent intent = getIntent();
        LatLng latLng = getGeocodeDataFromIntent(intent);

        Bundle args = new Bundle();
        args.putDouble("latitude", latLng.latitude);
        args.putDouble("longitude", latLng.longitude);

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_home_navHost);
        navHost.getNavController().setGraph(R.navigation.home_nav_graph, args);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navHost.getNavController());


        /**

         RestaurantRepository repo = new RestaurantRepository();
         String filter = "circle:" + latLng.longitude + "," + latLng.latitude + ",5000";
         Log.w("CIRCLE", filter);
         String categories = "catering.restaurant";
         String filter2 = "circle:12.429998,37.797323,5000";
         Log.w("CIRCLE2", filter2);
         int limit = 20;
         String apiKey = "7537b2a7bce846d7b1b21210d7d3f773";
         repo.searchRestaurant(
         categories,
         filter,
         limit,
         apiKey
         );
         **/
    }

    private LatLng getGeocodeDataFromIntent(Intent intent) {
        return new LatLng(
                Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lat"))),
                Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("lng"))));
    }

    private void initBundleToHomeFragment(LatLng latLng, NavController navController) {
        Bundle args = new Bundle();
        args.putDouble("latitude", latLng.latitude);     // Replace with actual latitude
        args.putDouble("longitude", latLng.longitude);   // Replace with actual longitude
        navController.navigate(R.id.nav_home, args);
    }

}