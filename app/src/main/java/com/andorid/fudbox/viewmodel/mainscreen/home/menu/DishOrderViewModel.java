package com.andorid.fudbox.viewmodel.mainscreen.home.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishQuantityRepository;

import java.util.List;

public class DishOrderViewModel extends ViewModel {

    private DishQuantityRepository repository;
    private LiveData<List<DishQuantity>> dishOrderLiveData;

    public DishOrderViewModel() {
    }

    public void init() {
        repository = new DishQuantityRepository();
        dishOrderLiveData = repository.getDishQuantitiesLiveData();
    }

    public LiveData<List<DishQuantity>> getDishOrderLiveData() {
        return dishOrderLiveData;
    }

    // Expose LiveData for observing the cart items
    public LiveData<List<DishQuantity>> getDishQuantitiesLiveData() {
        return repository.getDishQuantitiesLiveData();
    }

    // Method to add items to the cart
    public void addToCart(Dish dish, int quantity) {
        repository.addToCart(dish, quantity);
    }
}

