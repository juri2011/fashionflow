package com.fashionflow.constant;

public enum SellStatus {
    SELLING("1"), //판매중
    SUSPENDED("2"), //판매중단
    SOLD_OUT("3"); //판매완료

    private final String code;

    SellStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
