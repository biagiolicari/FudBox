package com.andorid.fudbox.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Order implements Serializable {
    private final Restaurant restaurant;
    private List<DishOrder> dishes = new ArrayList<>();

    public Order(Restaurant restaurant, List<DishOrder> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Order(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<DishOrder> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishOrder> dishes) {
        this.dishes = dishes;
    }

    public Double getTotalPrice() {
        Double totalPrice = 0.0;
        for (DishOrder dq : dishes) {
            Double dishPrice = dq.getDish().getPrice() * dq.getQuantity();
            totalPrice += dishPrice;
        }
        return totalPrice;
    }

    public void addDishAndQuantity(DishOrder dishOrder) {
        for (DishOrder dq : dishes) {
            if (dq.getDish().equals(dishOrder.getDish())) {
                int newQuantity = dq.getQuantity() + dishOrder.getQuantity();
                dq.setQuantity(newQuantity);
                return;
            }
        }
        // Dish is not in the order, must be added
        if (dishOrder.getQuantity() > 0) {
            dishes.add(dishOrder);
        }
    }

    public int getDishQuantity(Dish dish) {
        for (DishOrder dq : dishes) {
            if (dq.getDish().getName().equals(dish.getName())) {
                return dq.getQuantity();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "restaurant='" + restaurant + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}

