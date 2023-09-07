package com.andorid.fudbox.repository.mainscreen.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private final FirebaseFirestore firestore;
    private final MutableLiveData<List<Order>> ordersLiveData;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;

    public UserRepository() {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        ordersLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.setValue(firebaseAuth.getCurrentUser());
        }
    }

    public void fetchOrdersFromFirestore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            firestore.collection("orders")
                    .whereEqualTo("userUID", uid)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Order> orders = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Convert Firestore document to your Order object
                            Map<String, Object> restaurantData = (Map<String, Object>) document.getData().get("restaurant");
                            Restaurant restaurant = deserializeRestaurant(restaurantData);
                            List<DishOrder> dishOrderList = deserializeDishOrder(document.getData());
                            String orderDate = document.getData().get("orderDate").toString();
                            orders.add(new Order(restaurant, dishOrderList, orderDate));
                        }

                        // Now, 'orders' contains the list of orders for the current user.
                        ordersLiveData.setValue(orders);
                    })
                    .addOnFailureListener(e -> {
                        Log.wtf("FIREBASE", e.getMessage());
                    });
        }
    }

    private void deserializeOrder(Map<String, Object> orderData) {
        Restaurant restaurant = deserializeRestaurant((Map<String, Object>) orderData.get("restaurant"));
        //List<DishOrder> dishOrders = deserializeDishOrders((List<Map<String, Object>>) orderData.get("dishes"));

        // Create the 'Order' object using the builder pattern
    }

    private Restaurant deserializeRestaurant(Map<String, Object> restaurantData) {
        return new Restaurant.Builder()
                .setName((String) restaurantData.get("name"))
                .setAddress((String) restaurantData.get("address"))
                .setCity((String) restaurantData.get("city"))
                .setLat((double) restaurantData.get("lat"))
                .setLng((double) restaurantData.get("lng"))
                .setUid((String) restaurantData.get("uid"))
                .build();
    }

    private List<DishOrder> deserializeDishOrder(Map<String, Object> orderData) {
        List<Map<String, Object>> dishesList = (List<Map<String, Object>>) orderData.get("dishes");
        List<DishOrder> dishOrderList = new ArrayList<>();
        if (dishesList != null) {
            for (Map<String, Object> dishData : dishesList) {
                int quantity = ((Long) dishData.get("quantity")).intValue();
                Map<String, Object> dishInfo = (Map<String, Object>) dishData.get("dish");
                if (dishInfo != null) {
                    String name = (String) dishInfo.get("name");
                    double price = (double) dishInfo.get("price");
                    String description = (String) dishInfo.get("description");
                    String type = (String) dishInfo.get("type");
                    Dish tmpDish = new Dish.Builder()
                            .setName(name)
                            .setDescription(description)
                            .setType(DishType.valueOf(type))
                            .setPrice(price)
                            .build();
                    dishOrderList.add(new DishOrder(tmpDish, quantity));
                }
            }
        }
        return dishOrderList;
    }


    public LiveData<List<Order>> getRecentOrderLiveData() {
        return this.ordersLiveData;
    }

}
