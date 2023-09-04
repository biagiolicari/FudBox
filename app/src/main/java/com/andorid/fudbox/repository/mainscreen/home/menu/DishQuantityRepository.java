package com.andorid.fudbox.repository.mainscreen.home.menu;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;

import java.util.ArrayList;
import java.util.List;

public class DishQuantityRepository {

    private final List<DishQuantity> dishQuantities;
    private MutableLiveData<List<DishQuantity>> dishQuantitiesLiveData;

    public DishQuantityRepository() {
        this.dishQuantitiesLiveData = new MutableLiveData<>();
        this.dishQuantities = new ArrayList<>();
    }

    public MutableLiveData<List<DishQuantity>> getDishQuantitiesLiveData() {
        return dishQuantitiesLiveData;
    }

    public void setDishQuantitiesLiveData(MutableLiveData<List<DishQuantity>> dishQuantitiesLiveData) {
        this.dishQuantitiesLiveData = dishQuantitiesLiveData;
    }

    public void addToCart(Dish dish, int quantity) {
        // Check if the dish is already in the cart
        boolean dishExists = false;
        DishQuantity dishQuantity = new DishQuantity(dish, quantity);
        for (DishQuantity cartItem : dishQuantities) {
            if (cartItem.equals(dish)) {
                // If the dish is already in the cart, update its quantity
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                dishExists = true;
                break; // Exit the loop since the dish is found
            }
        }

        // If the dish is not in the cart, add it as a new item
        if (!dishExists) {
            dishQuantities.add(new DishQuantity(dish, quantity));
        }

        // Update the LiveData with the modified list
        dishQuantitiesLiveData.setValue(dishQuantities);
    }

}
