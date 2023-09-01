package com.andorid.fudbox.utils;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class LatLngDistanceCalculator {
    private static final double EARTH_RADIUS_METERS = 6371000.0; // Earth's radius in meters

    private LatLngDistanceCalculator() {
        // Private constructor to prevent instantiation
    }

    public static String calculateDistance(LatLng point1, LatLng point2) {
        double lat1 = Math.toRadians(point1.latitude);
        double lon1 = Math.toRadians(point1.longitude);
        double lat2 = Math.toRadians(point2.latitude);
        double lon2 = Math.toRadians(point2.longitude);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceInMeters = EARTH_RADIUS_METERS * c;

        // Round the distance to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(distanceInMeters) + "mt";
    }
}
