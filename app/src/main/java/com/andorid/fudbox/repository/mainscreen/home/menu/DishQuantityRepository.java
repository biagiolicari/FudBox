package com.andorid.fudbox.repository.mainscreen.home.menu;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;

public class DishQuantityRepository {

    private DishQuantity dq;
    private MutableLiveData<DishQuantity> dishQuantitiesLiveData;

    public DishQuantityRepository() {
        this.dishQuantitiesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DishQuantity> getDishQuantityLiveData() {
        return dishQuantitiesLiveData;
    }

    public void addItemToCart(Dish dish, int quantity) {
        DishQuantity dq = new DishQuantity(dish, quantity);
        this.dishQuantitiesLiveData.setValue(dq);
    }

    public void addItemToCart(DishQuantity dq){
        this.dishQuantitiesLiveData.setValue(dq);
    }



}
