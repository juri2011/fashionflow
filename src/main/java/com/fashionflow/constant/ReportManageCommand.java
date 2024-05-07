package com.fashionflow.constant;

public enum ReportManageCommand {
    SUSPEND("중지"), DELETE("삭제"), ETC("직접처리");

    private final String description;

    ReportManageCommand(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
