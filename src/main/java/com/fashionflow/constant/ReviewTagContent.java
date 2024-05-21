package com.fashionflow.constant;
public enum ReviewTagContent {
    FRIENDLY("친절해요"),
    AFFORDABLE("저렴해요"),
    FAST_DELIVERY("배송이 빨라요");

    // 각 열거형 상수에 대한 설명
    private final String description;

    // 생성자
    ReviewTagContent(String description) {
        this.description = description;
    }

    // 설명을 반환하는 getter 메서드
    public String getDescription() {
        return description;
    }
}
