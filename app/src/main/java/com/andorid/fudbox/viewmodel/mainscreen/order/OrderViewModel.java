package com.andorid.fudbox.viewmodel.mainscreen.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.repository.mainscreen.order.OrderRepository;

import java.util.List;


public class OrderViewModel extends ViewModel {
    private final OrderRepository orderRepository = OrderRepository.getInstance();
    private MutableLiveData<Order> orderLiveData = new MutableLiveData<>();


    public LiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    public void buildOrder(List<DishQuantity> dishes, String restaurant) {
        orderRepository.buildOrder(dishes, restaurant);
        orderLiveData = orderRepository.getOrderMutableLiveData();
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
