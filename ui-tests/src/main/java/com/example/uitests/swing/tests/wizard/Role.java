package com.example.uitests.swing.tests.wizard;

public enum Role {
    PRIVATE,
    EMPLOYEE;

    @Override
    public String toString() {
        return switch (this) {
            case PRIVATE -> "Private";
            case EMPLOYEE -> "Employee";
        };
    }
}
