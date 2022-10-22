package org.security.service.dto;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    private Roles(final String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}