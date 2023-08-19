package com.andorid.fudbox.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Menu {
    @Builder.Default private List<Dish> menu = new ArrayList<>();

    public void add(Dish dish) {
        this.menu.add(dish);
    }
}
