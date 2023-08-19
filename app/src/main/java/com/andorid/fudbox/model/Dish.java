package com.andorid.fudbox.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Dish {
    @Builder.Default private String name = "N/A";
    @Builder.Default private float price = 0.0f;
    private DishType type;
    @Builder.Default private String imgPath = "N/A";
}