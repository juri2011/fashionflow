package com.fashionflow.constant;

public enum ReportManageCommand {
    SUSPEND("중지"), DELETE("삭제"), ETC("직접처리");

    // 각 열거형 상수에 대한 설명
    private final String description;

    // 생성자
    ReportManageCommand(String description) {
        this.description = description;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
