package com.andorid.fudbox.model;

import java.util.List;

public class Order {
    private final Cart cart;
    private final String date;
    private final String userUid;
    private final String deliveryAddress;

    public Order(Cart cart, String date, String userUid, String deliveryAddress) {
        this.cart = cart;
        this.date = date;
        this.userUid = userUid;
        this.deliveryAddress = deliveryAddress;
    }

    public Cart getCart() {
        return cart;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDate() {
        return date;
    }

    public String getUserUid() {
        return userUid;
    }

    public Double getTotalCostOfOrder() {
        return cart.getTotalPrice();
    }

    public List<DishOrder> getOrderedDish() {
        return cart.getDishes();
    }

    public int getNumberOfDishOrdered() {
        return cart.getNumberOfDishOrdered();
    }

    public String getRestaurantName() {
        return cart.getRestaurant().getName();
    }

    public String getRestaurantAddress() {
        return cart.getRestaurant().getAddress();
    }


}
