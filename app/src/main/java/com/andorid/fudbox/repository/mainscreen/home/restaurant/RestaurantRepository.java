package com.andorid.fudbox.repository.mainscreen.home.restaurant;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.model.restaurant.RestaurantFeature;
import com.andorid.fudbox.model.restaurant.RestaurantJsonObject;
import com.andorid.fudbox.utils.Resource;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {
    private static final String BASE_URL = "https://api.geoapify.com/";

    private static final String CATEGORIES = "catering.restaurant,catering.fast_food";
    private static final int LIMIT = 30;
    private static final String API = "7537b2a7bce846d7b1b21210d7d3f773";
    private final IPlacesAPI iPlacesAPI;
    private final MutableLiveData<Resource<List<Restaurant>>> restaurantMutableLiveData;

    public RestaurantRepository() {
        restaurantMutableLiveData = new MutableLiveData<>();

        iPlacesAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IPlacesAPI.class);
    }

    public MutableLiveData<Resource<List<Restaurant>>> getRestaurantMutableLiveData() {
        return restaurantMutableLiveData;
    }

    public void searchRestaurant(String filter) {
        iPlacesAPI.getPlaces(CATEGORIES, filter, LIMIT, API)
                .enqueue(new Callback<RestaurantJsonObject>() {
            @Override
            public void onResponse(Call<RestaurantJsonObject> call, Response<RestaurantJsonObject> response) {
                if (response.isSuccessful()) {
                    List<RestaurantFeature> restaurantFeatures = response.body().getFeatures();
                    List<Restaurant> restaurants = restaurantFeatures.stream()
                            .map(item -> new Restaurant.Builder()
                                    .setName(item.getProperties().getName())
                                    .setAddress(item.getProperties().getAddressLine2())
                                    .setCity(item.getProperties().getCity())
                                    .setUid(item.getProperties().getPlaceId())
                                    .setLat(item.getProperties().getLat())
                                    .setLng(item.getProperties().getLon())
                                    .build())
                            .collect(Collectors.toList());
                    restaurantMutableLiveData.setValue(Resource.success(restaurants, null));
                }
            }

            @Override
            public void onFailure(Call<RestaurantJsonObject> call, Throwable t) {
                restaurantMutableLiveData.setValue(Resource.error(t.getLocalizedMessage().toString(), null));
            }
        });
    }

}
