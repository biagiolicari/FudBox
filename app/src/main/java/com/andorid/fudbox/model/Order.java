package com.andorid.fudbox.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Order implements Serializable {
    private final String restaurant;
    private List<DishQuantity> dishes;

    public Order(String restaurant, List<DishQuantity> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Order(String restaurant) {
        this.restaurant = restaurant;
        this.dishes = new ArrayList<>();
    }

    public String getRestaurant() {
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
        return "Order{" +
                "restaurant='" + restaurant + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}

