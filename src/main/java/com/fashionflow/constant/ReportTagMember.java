package com.fashionflow.constant;

public enum ReportTagMember {
    DISRESPECTFUL("Disrespectful"),
    FRAUD("Fraud");

    private final String description;

    ReportTagMember(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
