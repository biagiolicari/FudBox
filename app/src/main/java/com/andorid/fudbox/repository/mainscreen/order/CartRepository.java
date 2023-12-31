package com.andorid.fudbox.repository.mainscreen.order;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartRepository {
    private static CartRepository cartRepository;
    private final MutableLiveData<Cart> cartLiveData;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    private CartRepository() {
        cartLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static CartRepository getInstance() {
        if (cartRepository == null) {
            cartRepository = new CartRepository();
        }
        return cartRepository;
    }

    public MutableLiveData<Cart> getCartLiveData() {
        return cartLiveData;
    }

    public Double getTotalPriceOfTheOrder() {
        return cartLiveData.getValue().getTotalPrice();
    }

    public void buildCart(DishOrder dishOrder, Restaurant restaurant) {
        Cart currentCart = cartLiveData.getValue();

        if (currentCart == null || !currentCart.getRestaurant().equals(restaurant)) {
            // If there's no current order or the restaurant is different, create a new order
            Cart cart = new Cart(restaurant);
            cart.addDishAndQuantity(dishOrder);
            cartLiveData.setValue(cart);
        } else {
            // If the order is from the same restaurant, update the dishes' quantities
            currentCart.addDishAndQuantity(dishOrder);
            cartLiveData.setValue(currentCart);
        }
    }

    public void clearCart() {
        cartLiveData.setValue(null);
    }

    public void removeDishFromCart(DishOrder dishOrder) {
        Cart currentCart = cartLiveData.getValue();

        if (currentCart != null) {
            currentCart.removeDishOrder(dishOrder);
            cartLiveData.setValue(currentCart);
        }
    }

    public void incrementDishQuantityFromCart(DishOrder dishOrder) {
        Cart currentCart = cartLiveData.getValue();

        if (currentCart != null) {
            currentCart.incrementDishOrder(dishOrder);
            cartLiveData.setValue(currentCart);
        }
    }

    public void decrementDishQuantityFromCart(DishOrder dishOrder) {
        Cart currentCart = cartLiveData.getValue();

        if (currentCart != null) {
            currentCart.decrementDishOrder(dishOrder);
            cartLiveData.setValue(currentCart);
        }
    }

    private String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}

