package com.andorid.fudbox.model.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantJsonObject {
    @SerializedName("type")
    private String type;

    @SerializedName("features")
    private List<RestaurantFeature> restaurantFeatures;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RestaurantFeature> getFeatures() {
        return restaurantFeatures;
    }

    public void setFeatures(List<RestaurantFeature> restaurantFeatures) {
        this.restaurantFeatures = restaurantFeatures;
    }

    // Getter methods
}

