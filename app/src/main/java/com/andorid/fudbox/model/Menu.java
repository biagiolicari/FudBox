package com.andorid.fudbox.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final List<Dish> dishList;

    public Menu() {
        dishList = new ArrayList<>();
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void add(Dish dish) {
        this.dishList.add(dish);
    }
}
