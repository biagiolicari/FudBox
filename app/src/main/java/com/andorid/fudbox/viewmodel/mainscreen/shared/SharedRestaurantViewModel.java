package com.andorid.fudbox.viewmodel.mainscreen.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Restaurant;

public class SharedRestaurantViewModel extends ViewModel {
    private MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Restaurant> getRestaurantMutableLiveData() {
        return restaurantMutableLiveData;
    }

    public void setRestaurantToLiveData(Restaurant restaurant) {
        this.restaurantMutableLiveData.setValue(restaurant);
    }

}
