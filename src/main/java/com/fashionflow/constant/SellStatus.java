package com.fashionflow.constant;

public enum SellStatus {
    SELLING("1"), //판매중
    SUSPENDED("2"), //판매중단
    SOLD_OUT("3"); //판매완료

    // 코드 값을 저장할 필드
    private final String code;

    // 생성자
    SellStatus(String code) {
        this.code = code;
    }

    // 코드 값을 반환하는 메서드
    public String getCode() {
        return code;
    }
}
