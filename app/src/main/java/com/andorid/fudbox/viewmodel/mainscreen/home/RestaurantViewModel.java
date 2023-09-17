package com.andorid.fudbox.viewmodel.mainscreen.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.repository.mainscreen.home.restaurant.RestaurantRepository;
import com.andorid.fudbox.utils.Resource;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    private static final int RADIUS = 6000;
    private static final String CIRCLE = "circle:";
    private RestaurantRepository restaurantRepository;
    private LiveData<Resource<List<Restaurant>>> restaurantsLiveData;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurantsLiveData() {
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
                filter
        );
    }
}
