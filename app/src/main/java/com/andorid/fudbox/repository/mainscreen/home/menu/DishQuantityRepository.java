package com.andorid.fudbox.repository.mainscreen.home.menu;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;

import java.util.ArrayList;
import java.util.List;

public class DishQuantityRepository {

    private List<DishQuantity> cartItems;
    private MutableLiveData<List<DishQuantity>> dishesQtyLiveData;

    public DishQuantityRepository() {
        this.dishesQtyLiveData = new MutableLiveData<>();
        this.cartItems = new ArrayList<>();
    }

    public MutableLiveData<List<DishQuantity>> getDishesQtyLiveData() {
        return dishesQtyLiveData;
    }

    public void setDishesQtyLiveData(MutableLiveData<List<DishQuantity>> dishesQtyLiveData) {
        this.dishesQtyLiveData = dishesQtyLiveData;
    }

    public void addToCart(Dish dish, int quantity) {
        // Check if the dish is already in the cart
        for (DishQuantity cartItem : cartItems) {
            if (cartItem.getDish().equals(dish)) {
                // If the dish is already in the cart, update its quantity
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                dishesQtyLiveData.setValue(cartItems);
            }
            // If the dish is not in the cart, add it as a new item
            cartItems.add(new DishQuantity(dish, quantity));
            dishesQtyLiveData.setValue(cartItems);
        }
    }

}
