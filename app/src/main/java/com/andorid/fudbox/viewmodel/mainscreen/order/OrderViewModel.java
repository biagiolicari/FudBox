package com.andorid.fudbox.viewmodel.mainscreen.order;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.repository.mainscreen.order.OrderRepository;


public class OrderViewModel extends ViewModel {
    private final OrderRepository orderRepository = OrderRepository.getInstance();
    private MutableLiveData<Order> orderLiveData = orderRepository.getOrderMutableLiveData();


    public LiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    public void buildOrder(DishOrder dishes, Restaurant restaurant) {
        orderRepository.buildOrder(dishes, restaurant);
        orderLiveData.setValue(orderRepository.getOrderMutableLiveData().getValue());
        Log.wtf("BUILDORDER", orderLiveData.getValue().toString());
    }

    public void clearOrder() {
        orderRepository.clearOrder();
    }

    public void removeDishFromOrder(Dish dishToRemove) {
        orderRepository.removeDishFromOrder(dishToRemove);
    }

    public void uploadToFireStore() {
        orderRepository.uploadToFireStore();
    }
}
