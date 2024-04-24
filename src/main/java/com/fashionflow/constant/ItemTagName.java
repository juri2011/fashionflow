package com.fashionflow.constant;

public enum ItemTagName {
    DIRECT_TRADE("1"), //직거래
    DELIVERY_TRADE("2"), //택배거래
    SAME_DAY_DELIVERY("3"), //당일배송
    INTERNATIONAL_DELIVERY("4"); //해외배송

    private final String code;

    ItemTagName(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
