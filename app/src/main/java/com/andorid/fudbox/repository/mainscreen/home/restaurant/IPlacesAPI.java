package com.andorid.fudbox.repository.mainscreen.home.restaurant;

import com.andorid.fudbox.model.restaurant.RestaurantJsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface IPlacesAPI {

    @GET("v2/places")
    Call<RestaurantJsonObject> getPlaces(
            @Query("categories") String categories,
            @Query(value = "filter", encoded = true) String filter,
            @Query("limit") int limit,
            @Query("apiKey") String apiKey
    );
}
