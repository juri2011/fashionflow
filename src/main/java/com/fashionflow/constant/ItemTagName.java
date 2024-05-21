package com.fashionflow.constant;

public enum ItemTagName {
    DIRECT_TRADE("1", "직거래 가능"),
    DELIVERY_TRADE("2", "택배거래"),
    SAME_DAY_DELIVERY("3", "당일배송"),
    INTERNATIONAL_DELIVERY("4", "해외배송가능");

    // 각 열거형 상수에 대한 코드 값
    private final String code;

    // 각 열거형 상수에 대한 설명
    private final String description;

    // 생성자
    ItemTagName(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 코드 값을 반환하는 getter 메서드
    public String getCode() {
        return code;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
