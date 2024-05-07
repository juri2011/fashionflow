package com.fashionflow.constant;

public enum ItemTagName {
    DIRECT_TRADE("1", "직거래 가능"),
    DELIVERY_TRADE("2", "택배거래"),
    SAME_DAY_DELIVERY("3", "당일배송"),
    INTERNATIONAL_DELIVERY("4", "해외배송가능");

    private final String code;
    private final String description;

    ItemTagName(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
