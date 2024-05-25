package com.fashionflow.constant;

public enum ReportTagItem {
    INAPPROPRIATE_CONTENT("부적절한 내용"),
    HARMFUL_CONTENT("유해한 콘텐츠"),
    SPAM_ADVERTISEMENT("스팸(광고성)"),
    PERSONAL_INFORMATION_BREACH("개인정보 침해");

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
