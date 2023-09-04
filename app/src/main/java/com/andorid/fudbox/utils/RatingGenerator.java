package com.andorid.fudbox.utils;

import java.util.Random;

public class RatingGenerator {
    private static final float MIN_RATING = 1.0f;
    private static final float MAX_RATING = 5.0f;
    private static final Random random = new Random();


    public static float generateRandomRating() {
        return MIN_RATING + (MAX_RATING - MIN_RATING) * random.nextFloat();
    }
}
