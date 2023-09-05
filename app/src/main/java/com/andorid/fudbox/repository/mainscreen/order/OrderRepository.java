package com.andorid.fudbox.repository.mainscreen.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderRepository {
    private static OrderRepository orderRepository;
    private final MutableLiveData<Order> orderMutableLiveData;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    private OrderRepository() {
        orderMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static OrderRepository getInstance() {
        if (orderRepository == null) {
            orderRepository = new OrderRepository();
        }
        return orderRepository;
    }

    public MutableLiveData<Order> getOrderMutableLiveData() {
        return orderMutableLiveData;
    }

    public Double getTotalPriceOfTheOrder() {
        return orderMutableLiveData.getValue().getTotalPrice();
    }

    /**
     * public void buildOrder2(List<DishQuantity> dishes, String restaurant) {
     * Order currentOrder = orderMutableLiveData.getValue();
     * <p>
     * if (currentOrder == null || !currentOrder.getRestaurant().equals(restaurant)) {
     * // If there's no current order or the restaurant is different, create a new order
     * orderMutableLiveData.setValue(new Order(restaurant, dishes));
     * } else {
     * // If the order is from the same restaurant, update the dishes' quantities
     * Map<String, DishQuantity> currentDishMap = currentOrder.getDishes().stream()
     * .collect(Collectors.toMap(dq -> dq.getDish().getName(), dq -> dq));
     * <p>
     * dishes.forEach(newDishQuantity -> {
     * String dishName = newDishQuantity.getDish().getName();
     * DishQuantity currentDishQuantity = currentDishMap.get(dishName);
     * <p>
     * if (currentDishQuantity != null) {
     * int updatedQuantity = currentDishQuantity.getQuantity() + newDishQuantity.getQuantity();
     * currentDishQuantity.setQuantity(updatedQuantity);
     * } else {
     * currentOrder.addDishAndQuantity(newDishQuantity.getDish(), newDishQuantity.getQuantity());
     * }
     * });
     * <p>
     * orderMutableLiveData.setValue(currentOrder);
     * }
     * }
     **/

    public void buildOrder(List<DishQuantity> dishes, Restaurant restaurant) {
        Order currentOrder = orderMutableLiveData.getValue();

        if (currentOrder == null || !currentOrder.getRestaurant().equals(restaurant)) {
            // If there's no current order or the restaurant is different, create a new order
            orderMutableLiveData.setValue(new Order(restaurant, dishes));
        } else {
            // If the order is from the same restaurant, update the dishes' quantities
            dishes.forEach(d -> {
                currentOrder.addDishAndQuantity(d.getDish(), d.getQuantity());
            });
            orderMutableLiveData.setValue(currentOrder);
        }
    }

    public void clearOrder() {
        orderMutableLiveData.setValue(null);
    }

    public void removeDishFromOrder(Dish dishToRemove) {
        Order currentOrder = orderMutableLiveData.getValue();

        if (currentOrder != null) {
            List<DishQuantity> modifiedDishes = currentOrder.getDishes().stream()
                    .filter(dishQuantity -> !dishQuantity.getDish().equals(dishToRemove))
                    .collect(Collectors.toList());

            Order modifiedOrder = new Order(currentOrder.getRestaurant());
            modifiedOrder.setDishes(modifiedDishes);

            orderMutableLiveData.setValue(modifiedOrder);
        }
    }

    public void uploadToFireStore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && orderMutableLiveData.getValue() != null) {
            String uid = currentUser.getUid();
            Order latestOrder = orderMutableLiveData.getValue();
            String restaurantName = latestOrder.getRestaurant().getName();
            List<DishQuantity> dishes = latestOrder.getDishes();

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("userUID", uid);
            orderData.put("restaurantName", restaurantName);
            orderData.put("dishes", dishes);

            firestore.collection("orders")
                    .add(orderData)
                    .addOnSuccessListener(documentReference -> {
                        Log.i("FIREBASE", "ORDER ADDED CORRECTLY");
                    })
                    .addOnFailureListener(e -> {
                        Log.wtf("FIREBASE", e.getMessage());
                    });
        }
    }
}

