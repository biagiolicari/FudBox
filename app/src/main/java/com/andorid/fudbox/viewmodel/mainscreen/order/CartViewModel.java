package com.andorid.fudbox.viewmodel.mainscreen.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.repository.mainscreen.order.CartRepository;


public class CartViewModel extends ViewModel {
    private final CartRepository cartRepository = CartRepository.getInstance();
    private final MutableLiveData<Cart> orderLiveData = cartRepository.getCartLiveData();


    public LiveData<Cart> getOrderLiveData() {
        return orderLiveData;
    }

    public void buildCart(DishOrder dishes, Restaurant restaurant) {
        cartRepository.buildCart(dishes, restaurant);
    }

    public void clearCart() {
        cartRepository.clearCart();
    }

    public void removeDishQuantityFromCart(DishOrder dishToRemove) {
        cartRepository.removeDishFromCart(dishToRemove);
    }

    public void incrementDishQuantityFromCart(DishOrder dishToRemove) {
        cartRepository.incrementDishQuantityFromCart(dishToRemove);
    }

    public void decrementDishQuantityFromCart(DishOrder dishToRemove) {
        cartRepository.decrementDishQuantityFromCart(dishToRemove);
    }
}
