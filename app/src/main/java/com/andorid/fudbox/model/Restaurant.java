package com.andorid.fudbox.model;

import java.io.Serializable;
import java.util.Objects;

public class Restaurant implements Serializable {
    private final String name;
    private final String address;
    private final String city;
    private final double lng;
    private final double lat;
    private final String uid;

    private Restaurant(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.uid = builder.uid;
        this.lat = builder.lat;
        this.lng = builder.lng;
        this.city = builder.city;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        if (Double.compare(that.lng, lng) != 0) return false;
        if (Double.compare(that.lat, lat) != 0) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(address, that.address)) return false;
        if (!Objects.equals(city, that.city)) return false;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private String name = "N/A";
        private String address = "N/A";
        private String city = "N/A";
        private double lng = 0.0;
        private double lat = 0.0;
        private String uid = "N/A";

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLng(double lng) {
            this.lng = lng;
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

}
