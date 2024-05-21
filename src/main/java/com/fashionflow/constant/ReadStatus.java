package com.fashionflow.constant;

public enum ReadStatus {
    READ("읽음"), UNREAD("안읽음");
    private final String description;

    // 각 열거형 상수에 대한 설명
    ReadStatus(String description) {
        this.description = description;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
