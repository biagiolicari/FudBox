package com.andorid.fudbox.model;

public class DishQuantity {
    public DishQuantity(Dish dish) {
        this.dish = dish;
        this.quantity = 0;
    }

    private final Dish dish;
    private int quantity;

    public DishQuantity(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementDishQuantity() {
        this.quantity++;
    }
}
