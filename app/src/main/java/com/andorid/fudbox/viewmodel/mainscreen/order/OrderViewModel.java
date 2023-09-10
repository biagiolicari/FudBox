package com.andorid.fudbox.viewmodel.mainscreen.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.repository.mainscreen.order.OrderRepository;

public class OrderViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private LiveData<Order> orderLiveData;

    public void init() {
        orderRepository = OrderRepository.getInstance();
        orderLiveData = orderRepository.getOrderMutableLiveData();
    }

    public LiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    public void uploadOrderToFirestore() {
        orderRepository.uploadToFireStore();
    }

    public void buildOrder(Cart cart, String deliveryPlace) {
        orderRepository.buildOrder(cart, deliveryPlace);
    }

    public long getTotalOrderPrice(){
        return orderLiveData.getValue().getTotalCostOfOrder().longValue();
    }
}
