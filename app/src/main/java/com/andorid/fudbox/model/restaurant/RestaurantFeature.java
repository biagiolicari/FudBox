package com.andorid.fudbox.model.restaurant;

import com.google.gson.annotations.SerializedName;

public class RestaurantFeature {
    @SerializedName("type")
    private String type;

    @SerializedName("properties")
    private RestaurantProperties restaurantProperties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RestaurantProperties getProperties() {
        return restaurantProperties;
    }

    public void setProperties(RestaurantProperties restaurantProperties) {
        this.restaurantProperties = restaurantProperties;
    }

    // Getter methods
}
