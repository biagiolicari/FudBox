package com.andorid.fudbox.viewmodel.mainscreen.home.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishQuantityRepository;

public class DishOrderViewModel extends ViewModel {

    private DishQuantityRepository repository;
    private MutableLiveData<DishQuantity> dishOrderLiveData;

    public DishOrderViewModel() {
    }

    public void init() {
        repository = new DishQuantityRepository();
        dishOrderLiveData = repository.getDishQuantityLiveData();
    }

    public LiveData<DishQuantity> getDishOrderLiveData() {
        return dishOrderLiveData;
    }

    // Expose LiveData for observing the cart items
    public LiveData<DishQuantity> getDishQuantitiesLiveData() {
        return repository.getDishQuantityLiveData();
    }

    // Method to add items to the cart
    public void addItemToCart(Dish dish, int quantity) {
        repository.addItemToCart(dish, quantity);
    }
}

