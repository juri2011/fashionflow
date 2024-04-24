package com.fashionflow.constant;
public enum ReviewTagContent {
    FRIENDLY("친절해요"),
    AFFORDABLE("저렴해요"),
    FAST_DELIVERY("배송이 빨라요");

    private final String description;

    ReviewTagContent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
