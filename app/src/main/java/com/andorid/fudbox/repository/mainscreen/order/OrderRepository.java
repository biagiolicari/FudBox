package com.andorid.fudbox.repository.mainscreen.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderRepository {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;
    private MutableLiveData<Order> orderMutableLiveData;
    private static OrderRepository orderRepository;

    private OrderRepository() {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        orderMutableLiveData =  new MutableLiveData<>();
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

    public void buildOrder(Cart cart, String deliveryPlace){
        Order order = new Order(cart,
                generateOrderFormattedDate(),
                firebaseAuth.getCurrentUser().getUid(),
                deliveryPlace
                );
        orderMutableLiveData.setValue(order);
    }

    public void uploadToFireStore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && orderMutableLiveData.getValue() != null) {
            String uid = currentUser.getUid();
            Order order = orderMutableLiveData.getValue();

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("order", order);

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

    private String generateOrderFormattedDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
