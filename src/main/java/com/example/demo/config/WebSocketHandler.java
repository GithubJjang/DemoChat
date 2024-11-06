package com.example.demo.config;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.ChatRoom;
import com.example.demo.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatRoomService manageService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = manageService.findRoomById(chatMessage.getId());

        Set<WebSocketSession> sessions=room.getSessions();

        // 1. 채팅방에 입장을 한 경우
        if(chatMessage.getState().equals("Enter")){

            // sessions에 Session을 추가한다.
            sessions.add(session);
            chatMessage.setContent("Greeting!");
            // ChatMessage를 브로드캐스팅을 한다.
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );

        }
        // 2. 채팅방에서 나간 경우
        else if (chatMessage.getState().equals("Exit")) {

            // sessions에서 Session을 삭제한다.
            sessions.remove(session);
            chatMessage.setContent("Bye~");
            // ChatMessage를 브로드캐스팅을 한다.
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );

        }
        // 3. 채팅방에 남아 있는 경우
        else{
            // ChatMessage를 브로드캐스팅을 한다.
            sendToEachSocket(sessions,message);
        }
    }

    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        sessions.parallelStream().forEach( roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 클라이언트와 WebSocket을 하자마자 실행이 되는 함수
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Socket Connect Success");

    }

    // 클라이언트와 WebSocket이 끊어지자마자 실행이 되는 함수.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //super.afterConnectionClosed(session, status);
    }
}
