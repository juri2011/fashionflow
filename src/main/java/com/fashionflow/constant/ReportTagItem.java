package com.fashionflow.constant;

public enum ReportTagItem {
    VIOLATION("규정위반");

    // 각 열거형 상수에 대한 설명
    private final String description;

    // 생성자
    ReportTagItem(String description) {
        this.description = description;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
