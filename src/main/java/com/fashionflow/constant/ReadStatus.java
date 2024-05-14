package com.fashionflow.constant;

public enum ReadStatus {
    READ("읽음"), UNREAD("안읽음");
    private final String description;

    ReadStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
