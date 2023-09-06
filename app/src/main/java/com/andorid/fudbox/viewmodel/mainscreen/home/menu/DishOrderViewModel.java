package com.andorid.fudbox.viewmodel.mainscreen.home.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishOrderRepository;

public class DishOrderViewModel extends ViewModel {

    private DishOrderRepository repository;
    private MutableLiveData<DishOrder> dishOrderLiveData;

    public DishOrderViewModel() {
    }

    public void init() {
        repository = new DishOrderRepository();
        dishOrderLiveData = repository.getDishQuantityLiveData();
    }

    public LiveData<DishOrder> getDishOrderLiveData() {
        return dishOrderLiveData;
    }

    // Expose LiveData for observing the cart items
    public LiveData<DishOrder> getDishQuantitiesLiveData() {
        return repository.getDishQuantityLiveData();
    }

    // Method to add items to the cart
    public void addItemToCart(Dish dish, int quantity) {
        repository.addItemToCart(dish, quantity);
    }
}

