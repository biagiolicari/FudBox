package com.andorid.fudbox.model;

import org.jetbrains.annotations.NotNull;

public class Dish {
    private final String name;
    private final float price;
    private final DishType type;
    private final String imgPath;

    private Dish(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.type = builder.type;
        this.imgPath = builder.imgPath;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    @NotNull
    public DishType getType() {
        return type;
    }

    @NotNull
    public String getImgPath() {
        return imgPath;
    }

    public static class Builder {
        private String name = "N/A";
        private float price = 0.0f;
        private DishType type;
        private String imgPath = "N/A";

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(float price) {
            this.price = price;
            return this;
        }

        public Builder setType(DishType type) {
            this.type = type;
            return this;
        }

        public Builder setImgPath(String imgPath) {
            this.imgPath = imgPath;
            return  this;
        }

        public Dish build() {
            return new Dish(this);
        }
    }
}