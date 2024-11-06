package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    // 채팅방 번호
    private String id;
    private String content;
    private String state;
}
