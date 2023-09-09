package com.andorid.fudbox.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cart implements Serializable {
    private Restaurant restaurant;
    private List<DishOrder> dishes = new ArrayList<>();
    private String orderDate;

    public Cart(Restaurant restaurant, List<DishOrder> dishes, String orderDate) {
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.orderDate = orderDate;
    }

    public Cart(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getOrderDate() {
        return orderDate;
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

    public int getNumberOfDishOrdered() {
        return dishes.size();
    }

    @Override
    public String toString() {
        return "Order{" +
                "restaurant='" + restaurant + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}

