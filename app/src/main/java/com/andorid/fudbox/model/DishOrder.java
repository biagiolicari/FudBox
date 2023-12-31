package com.andorid.fudbox.model;

import java.io.Serializable;

public class DishOrder implements Serializable {
    private final Dish dish;
    private int quantity;

    public DishOrder(Dish dish) {
        this.dish = dish;
        this.quantity = 0;
    }

    public DishOrder(Dish dish, int quantity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DishOrder that = (DishOrder) o;

        if (quantity != that.quantity) return false;
        return dish.equals(that.dish);
    }

    @Override
    public int hashCode() {
        int result = dish.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "DishQuantity{" +
                "dish=" + dish +
                ", quantity=" + quantity +
                '}';
    }
}
