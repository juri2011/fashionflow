package com.fashionflow.constant;

public enum ReportTagMember {
    DISRESPECTFUL("비매너"),
    FRAUD("사기");

    private final String description;

    ReportTagMember(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
