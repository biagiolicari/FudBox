package com.andorid.fudbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DishQuantity {
    private Dish dish;
    @Builder.Default private int quantity = 0;

    public void incrementDishQuantity(){
        this.quantity++;
    }
}
