package com.andorid.fudbox.model;


import com.andorid.fudbox.model.restaurant.RestaurantFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Order implements Serializable {
    private final RestaurantFeature restaurantFeature;
    private final ArrayList<DishQuantity> dishes;

    public Order(RestaurantFeature restaurantFeature, ArrayList<DishQuantity> dishes) {
        this.restaurantFeature = restaurantFeature;
        this.dishes = dishes;
    }

    public Order(RestaurantFeature restaurantFeature) {
        this.restaurantFeature = restaurantFeature;
        this.dishes = new ArrayList<>();
    }

    public RestaurantFeature getRestaurant() {
        return restaurantFeature;
    }

    public List<DishQuantity> getDishes() {
        return dishes;
    }

    public float getTotalPrice() {
        float totalPrice = 0.0f;
        for (DishQuantity dq : dishes) {
            float dishPrice = dq.getDish().getPrice() * dq.getQuantity();
            totalPrice += dishPrice;
        }
        return totalPrice;
    }

    public void addDish(Dish dish, int num) {
        DishQuantity dishesToAdd = new DishQuantity(dish, num);
        this.dishes.add(dishesToAdd);
    }

    public void setDishQuantity(Dish dish, int quantity) {
        for (DishQuantity dq : dishes) {
            if (dq.getDish().getName().equals(dish.getName())) {
                if (quantity <= 0) {
                    dishes.remove(dq);
                } else {
                    dq.setQuantity(quantity);
                }
                return;
            }
        }
        // Dish is not in the order, must be added
        if (quantity > 0) {
            DishQuantity newDishQuantity = new DishQuantity(dish, quantity);
            dishes.add(newDishQuantity);
        }
    }

    public int getDishQuantity(Dish dish) {
        for (DishQuantity dq : dishes) {
            if (dq.getDish().getName().equals(dish.getName())) {
                return dq.getQuantity();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder orderStr = new StringBuilder();
        for (DishQuantity dq : dishes) {
            Dish dish = dq.getDish();
            orderStr.append(dish.getName()).append(":").append(dq.getQuantity()).append(" ");
        }
        return orderStr.toString();
    }
}

