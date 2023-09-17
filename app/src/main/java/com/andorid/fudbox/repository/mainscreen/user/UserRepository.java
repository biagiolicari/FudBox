package com.andorid.fudbox.repository.mainscreen.user;

import static com.andorid.fudbox.utils.Constants.CART;
import static com.andorid.fudbox.utils.Constants.COLLECTION_PATH;
import static com.andorid.fudbox.utils.Constants.DESCRIPTION;
import static com.andorid.fudbox.utils.Constants.DISH;
import static com.andorid.fudbox.utils.Constants.DISHES;
import static com.andorid.fudbox.utils.Constants.LAT;
import static com.andorid.fudbox.utils.Constants.LNG;
import static com.andorid.fudbox.utils.Constants.NAME;
import static com.andorid.fudbox.utils.Constants.PRICE;
import static com.andorid.fudbox.utils.Constants.QUANTITY;
import static com.andorid.fudbox.utils.Constants.RESTAURANT;
import static com.andorid.fudbox.utils.Constants.RESTAURANT_ADDRESS;
import static com.andorid.fudbox.utils.Constants.RESTAURANT_CITY;
import static com.andorid.fudbox.utils.Constants.RESTAURANT_NAME;
import static com.andorid.fudbox.utils.Constants.TYPE;
import static com.andorid.fudbox.utils.Constants.UID;
import static com.andorid.fudbox.utils.Constants.USER_UID;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.utils.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private final FirebaseFirestore firestore;
    private final MutableLiveData<Resource<List<Order>>> ordersLiveData;
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
            firestore.collection(COLLECTION_PATH)
                    .whereEqualTo(USER_UID, uid)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Order> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> orderDocument = document.getData();
                            Order order = deserializeOrder(orderDocument, uid);
                            orders.add(order);
                        }
                        ordersLiveData.setValue(Resource.success(orders, null));
                    }).addOnFailureListener(e -> ordersLiveData.setValue(Resource.error(e.getLocalizedMessage(), null)));
        }
    }

    private Order deserializeOrder(Map<String, Object> orderData, String uid) {
        Map<String, Object> cartDocument = (Map<String, Object>) orderData.get(CART);
        Restaurant restaurant = deserializeRestaurant((Map<String, Object>) cartDocument.get(RESTAURANT));
        List<DishOrder> dishOrders = deserializeDishOrder(cartDocument);
        String orderDate = (String) orderData.get("date");
        String deliveryAddress = (String) orderData.get("deliveryAddress");
        Cart cart = new Cart(restaurant, dishOrders);
        return new Order(cart, orderDate, uid, deliveryAddress);
    }

    private Restaurant deserializeRestaurant(Map<String, Object> restaurantData) {
        return new Restaurant.Builder()
                .setName((String) restaurantData.get(RESTAURANT_NAME))
                .setAddress((String) restaurantData.get(RESTAURANT_ADDRESS))
                .setCity((String) restaurantData.get(RESTAURANT_CITY))
                .setLat((double) restaurantData.get(LAT))
                .setLng((double) restaurantData.get(LNG))
                .setUid((String) restaurantData.get(UID))
                .build();
    }

    private List<DishOrder> deserializeDishOrder(Map<String, Object> orderData) {
        List<Map<String, Object>> dishesList = (List<Map<String, Object>>) orderData.get(DISHES);
        List<DishOrder> dishOrderList = new ArrayList<>();
        if (dishesList != null) {
            for (Map<String, Object> dishData : dishesList) {
                int quantity = ((Long) dishData.get(QUANTITY)).intValue();
                Map<String, Object> dishInfo = (Map<String, Object>) dishData.get(DISH);
                if (dishInfo != null) {
                    String name = (String) dishInfo.get(NAME);
                    double price = (double) dishInfo.get(PRICE);
                    String description = (String) dishInfo.get(DESCRIPTION);
                    String type = (String) dishInfo.get(TYPE);
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


    public LiveData<Resource<List<Order>>> getRecentOrderLiveData() {
        return this.ordersLiveData;
    }

}
