package com.andorid.fudbox.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Order implements Serializable {
    private final Restaurant restaurant;
    private List<DishQuantity> dishes;

    public Order(Restaurant restaurant, List<DishQuantity> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Order(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.dishes = new ArrayList<>();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<DishQuantity> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishQuantity> dishes) {
        this.dishes = dishes;
    }

    public Double getTotalPrice() {
        Double totalPrice = 0.0;
        for (DishQuantity dq : dishes) {
            Double dishPrice = dq.getDish().getPrice() * dq.getQuantity();
            totalPrice += dishPrice;
        }
        return totalPrice;
    }

    public void addDishAndQuantity(Dish dish, int quantity) {
        for (DishQuantity dq : dishes) {
            if (dq.getDish().equals(dish)) {
                int newQuantity = dq.getQuantity() + quantity;
                dq.setQuantity(newQuantity);
                }
                return;
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
        return "Order{" +
                "restaurant='" + restaurant + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}

