package com.andorid.fudbox.repository.mainscreen.home.menu;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;

public class DishOrderRepository {

    private DishOrder dq;
    private MutableLiveData<DishOrder> dishQuantitiesLiveData;

    public DishOrderRepository() {
        this.dishQuantitiesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DishOrder> getDishQuantityLiveData() {
        return dishQuantitiesLiveData;
    }

    public void addItemToCart(Dish dish, int quantity) {
        DishOrder dq = new DishOrder(dish, quantity);
        this.dishQuantitiesLiveData.setValue(dq);
    }

    public void addItemToCart(DishOrder dq){
        this.dishQuantitiesLiveData.setValue(dq);
    }



}
