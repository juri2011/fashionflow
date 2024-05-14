package com.fashionflow.constant;

public enum ItemStatus {
    NEW("1"), // 새상품(미개봉)
    NO_SIGNS_OF_USE("2"), // 사용감 없음
    SOME_SIGNS_OF_USE("3"), // 사용감 적음
    SIGNIFICANT_SIGNS_OF_USE("4"), // 사용감 많음
    FAULTY("5"); // 파손, 고장 상품

    private final String code;

    ItemStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

