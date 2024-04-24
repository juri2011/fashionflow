package com.fashionflow.constant;

public enum ReportTagItem {
    VIOLATION("규정위반");

    private final String description;

    ReportTagItem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
