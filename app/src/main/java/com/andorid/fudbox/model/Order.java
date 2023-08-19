package com.andorid.fudbox.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Builder
public class Order implements Serializable {
    private Restaurant restaurant;
    private ArrayList<DishQuantity> dishes;

    public float getTotalPrice() {
        float totalPrice = 0.0f;
        for (DishQuantity dq : dishes) {
            float dishPrice = dq.getDish().getPrice() * dq.getQuantity();
            totalPrice += dishPrice;
        }
        return totalPrice;
    }

    public void addDish(Dish dish, int num) {
        DishQuantity dishesToAdd = DishQuantity.builder()
                .dish(dish)
                .quantity(num)
                .build();
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
            DishQuantity newDishQuantity = DishQuantity.builder()
                    .dish(dish)
                    .quantity(quantity)
                    .build();
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

