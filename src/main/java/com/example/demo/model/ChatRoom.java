package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoom {

    // 중요!
    // 각각의 ChatRoom마다 개별적인 session집합을 가지고 있어야 함!
    private Set<WebSocketSession> sessions = new HashSet<>();
    private String id;
    private String name;
}
