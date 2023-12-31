package com.andorid.fudbox.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Dish implements Serializable {
    private final String name;
    private final Double price;
    private final DishType type;
    private final String description;

    private Dish(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.type = builder.type;
        this.description = builder.description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @NotNull
    public DishType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish dish = (Dish) o;

        if (!name.equals(dish.name)) return false;
        if (!price.equals(dish.price)) return false;
        if (type != dish.type) return false;
        return description.equals(dish.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }


    public static class Builder {
        private String name = "N/A";
        private Double price = 0.0;
        private DishType type;
        private String description = "N/A";

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setType(DishType type) {
            this.type = type;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Dish build() {
            return new Dish(this);
        }
    }
}