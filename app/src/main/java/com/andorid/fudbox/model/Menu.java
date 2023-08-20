package com.andorid.fudbox.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final List<Dish> menu = new ArrayList<>();

    public Menu() {
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void add(Dish dish) {
        this.menu.add(dish);
    }
}
