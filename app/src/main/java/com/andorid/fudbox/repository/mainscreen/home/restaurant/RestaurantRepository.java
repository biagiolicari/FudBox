package com.andorid.fudbox.repository.mainscreen.home.restaurant;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.restaurant.RestaurantFeature;
import com.andorid.fudbox.model.restaurant.RestaurantJsonObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {
    private static final String BASE_URL = "https://api.geoapify.com/";
    private final IPlacesAPI iPlacesAPI;
    private final MutableLiveData<List<RestaurantFeature>> restaurantMutableLiveData;

    public RestaurantRepository() {
        restaurantMutableLiveData = new MutableLiveData<>();
        /**
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        **/
        iPlacesAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IPlacesAPI.class);
    }

    public MutableLiveData<List<RestaurantFeature>> getRestaurantMutableLiveData() {
        return restaurantMutableLiveData;
    }

    public void searchRestaurant(String categories, String filter, int limit, String apiKey) {
        iPlacesAPI.getPlaces(categories, filter, limit, apiKey).enqueue(new Callback<RestaurantJsonObject>() {
            @Override
            public void onResponse(Call<RestaurantJsonObject> call, Response<RestaurantJsonObject> response) {
                if (response.isSuccessful()) {
                    RestaurantJsonObject restaurantJsonObject = response.body();
                    restaurantMutableLiveData.setValue(restaurantJsonObject.getFeatures());
                }
            }

            @Override
            public void onFailure(Call<RestaurantJsonObject> call, Throwable t) {
                restaurantMutableLiveData.setValue(null);
            }
        });
    }

}
