package com.fashionflow.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private String roomId;
    private String writer;
    private String message;
}
