package com.andorid.fudbox.viewmodel.mainscreen.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andorid.fudbox.model.restaurant.RestaurantFeature;
import com.andorid.fudbox.repository.mainscreen.home.restaurant.RestaurantRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    private static final String CATEGORIES = "catering.restaurant,catering.fast_food";
    private static final int LIMIT = 30;
    private static final String API = "7537b2a7bce846d7b1b21210d7d3f773";
    private static final int RADIUS = 6000;

    private static final String CIRCLE = "circle:";
    private RestaurantRepository restaurantRepository;
    private LiveData<List<RestaurantFeature>> restaurantsLiveData;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<RestaurantFeature>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public void init() {
        restaurantRepository = new RestaurantRepository();
        restaurantsLiveData = restaurantRepository.getRestaurantMutableLiveData();
    }

    public void searchRestaurants(LatLng latLng) {
        String filter = CIRCLE +
                latLng.longitude +
                "," +
                latLng.latitude +
                "," +
                RADIUS;

        restaurantRepository.searchRestaurant(
                CATEGORIES,
                filter,
                LIMIT,
                API
        );
    }
}
