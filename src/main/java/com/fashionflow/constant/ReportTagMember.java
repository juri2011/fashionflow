package com.fashionflow.constant;

public enum ReportTagMember {
    DISRESPECTFUL("비매너"),
    FRAUD("사기");

    // 각 열거형 상수에 대한 설명
    private final String description;

    // 생성자
    ReportTagMember(String description) {
        this.description = description;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
