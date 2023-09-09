package com.andorid.fudbox.repository.mainscreen.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class CartRepository {
    private static CartRepository cartRepository;
    private final MutableLiveData<Cart> orderMutableLiveData;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    private CartRepository() {
        orderMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static CartRepository getInstance() {
        if (cartRepository == null) {
            cartRepository = new CartRepository();
        }
        return cartRepository;
    }

    public MutableLiveData<Cart> getOrderMutableLiveData() {
        return orderMutableLiveData;
    }

    public Double getTotalPriceOfTheOrder() {
        return orderMutableLiveData.getValue().getTotalPrice();
    }

    public void buildOrder(DishOrder dishOrder, Restaurant restaurant) {
        Cart currentCart = orderMutableLiveData.getValue();

        if (currentCart == null || !currentCart.getRestaurant().equals(restaurant)) {
            // If there's no current order or the restaurant is different, create a new order
            Cart cart = new Cart(restaurant);
            cart.addDishAndQuantity(dishOrder);
            orderMutableLiveData.setValue(cart);
        } else {
            // If the order is from the same restaurant, update the dishes' quantities
            currentCart.addDishAndQuantity(dishOrder);
            orderMutableLiveData.setValue(currentCart);
        }
    }

    public void clearOrder() {
        orderMutableLiveData.setValue(null);
    }

    public void removeDishFromOrder(Dish dishToRemove) {
        Cart currentCart = orderMutableLiveData.getValue();

        if (currentCart != null) {
            List<DishOrder> modifiedDishes = currentCart.getDishes().stream()
                    .filter(dishQuantity -> !dishQuantity.getDish().equals(dishToRemove))
                    .collect(Collectors.toList());

            Cart modifiedCart = new Cart(currentCart.getRestaurant());
            modifiedCart.setDishes(modifiedDishes);

            orderMutableLiveData.setValue(modifiedCart);
        }
    }

    public void uploadToFireStore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && orderMutableLiveData.getValue() != null) {
            String uid = currentUser.getUid();
            Cart latestCart = orderMutableLiveData.getValue();
            String restaurantName = latestCart.getRestaurant().getName();
            List<DishOrder> dishes = latestCart.getDishes();

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("userUID", uid);
            orderData.put("restaurant", latestCart.getRestaurant());
            orderData.put("dishes", dishes);
            orderData.put("orderDate", getFormattedDate());

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

    private String getFormattedDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}

