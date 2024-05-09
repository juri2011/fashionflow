package com.fashionflow.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {
    
    //연결 정보를 담을 리스트
    private static List<WebSocketSession> list = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        //payload : 전송되는 데이터
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for(WebSocketSession sess : list){
            sess.sendMessage(message);
        }
    }

    /* 클라이언트 접속 시 호출 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);
        
        log.info(session + "클라이언트 접속");
    }

    /* 클라이언트 접속 해제 시 호출 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");

        list.remove(session);
    }
}