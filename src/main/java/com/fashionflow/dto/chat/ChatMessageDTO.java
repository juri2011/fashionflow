package com.fashionflow.dto.chat;

import com.fashionflow.constant.ReadStatus;
import com.fashionflow.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private String roomId;
    private String writer;
    private String message;

    @JsonIgnore
    private LocalDateTime msgDate;
    private String msgDateFormat;
    private ReadStatus readStatus;

    public static ChatMessageDTO entityToDTO(ChatMessage chatMessage){
        return ChatMessageDTO.builder()
                .roomId(chatMessage.getChatRoom().getUuid())
                .writer(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .msgDate(chatMessage.getSendDate())
                .msgDateFormat(chatMessage.getSendDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .readStatus(chatMessage.getReadStatus())
                .build();
    }
}
