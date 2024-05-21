package com.fashionflow.constant;

public enum ItemStatus {
    NEW("1"), // 새상품(미개봉)
    NO_SIGNS_OF_USE("2"), // 사용감 없음
    SOME_SIGNS_OF_USE("3"), // 사용감 적음
    SIGNIFICANT_SIGNS_OF_USE("4"), // 사용감 많음
    FAULTY("5"); // 파손, 고장 상품

    // 코드 값을 저장할 필드
    private final String code;

    // 생성자
    ItemStatus(String code) {
        this.code = code;
    }

    // 코드값 반환 메소드
    public String getCode() {
        return code;
    }
}

