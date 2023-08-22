package com.andorid.fudbox.model;

import java.io.Serializable;

public class User implements Serializable {
    private final Boolean logged;
    private final String username;
    private final String uuid;

    private User(Builder builder) {
        this.logged = builder.logged;
        this.uuid = builder.uuid;
        this.username = builder.username;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public Boolean getLogged() {
        return logged;
    }

    public static class Builder {
        private String username;
        private String uuid;
        private Boolean logged = Boolean.FALSE;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setLogged(Boolean logged) {
            this.logged = logged;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
