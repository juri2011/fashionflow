package com.fashionflow.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private String roomId;
    private String writer;
    private String message;
    //private LocalDateTime msgDate;
}
