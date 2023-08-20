package com.andorid.fudbox.model;

public class Restaurant {
    private final String name;
    private final String address;
    private final String city;
    private final String suburb;
    private final String phone;
    private final Float lat;

    private Restaurant(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.city = builder.city;
        this.suburb = builder.suburb;
        this.phone = builder.phone;
        this.lat = builder.lat;
        this.log = builder.log;
    }

    private final Float log;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getPhone() {
        return phone;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLog() {
        return log;
    }

    public static class Builder {
        private String name = "N/A";
        private String address = "N/A";
        private String city = "N/A";
        private String suburb = "N/A";
        private String phone = "N/A";
        private Float lat = 0.0f;
        private Float log = 0.0f;

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

        public Builder setSuburb(String suburb) {
            this.suburb = suburb;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setLat(Float lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLog(Float log) {
            this.log = log;
            return this;
        }

        public Restaurant build(){
            return new Restaurant(this);
        }

    }
}
