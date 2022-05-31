package com.epam.esm.entity;

public enum Permission {
    GIFT_CERTIFICATES_CREATE("gift-certificates:create"),
    GIFT_CERTIFICATES_UPDATE("gift-certificates:update"),
    GIFT_CERTIFICATES_DELETE("gift-certificates:delete"),
    TAGS_CREATE("tags:create"),
    TAGS_DELETE("tags:delete"),
    USERS_CREATE("users:create"),
    USERS_READ("users:read"),
    ORDERS_CREATE("orders:create"),
    ORDERS_READ("orders:read"),
    ORDERS_DELETE("orders:delete"),
    AUTH_SIGN_UP("auth:sign_up");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
