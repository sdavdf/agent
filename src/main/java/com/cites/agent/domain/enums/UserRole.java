package com.cites.agent.domain.enums;

public enum UserRole {

    PLATFORM_ADMIN,
    TENANT_ADMIN,
    PROFESSIONAL,
    STAFF,
    CLIENT;

    public boolean isAdmin() {
        return this == PLATFORM_ADMIN || this == TENANT_ADMIN;
    }

    public boolean isStaff() {
        return this == STAFF || this == TENANT_ADMIN;
    }

    public boolean canManageAppointments() {
        return this != CLIENT;
    }
}
