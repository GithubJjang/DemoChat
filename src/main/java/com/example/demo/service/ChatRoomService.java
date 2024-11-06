package com.example.demo.service;

import com.example.demo.dto.ChatRoomDTO;
import com.example.demo.model.ChatRoom;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    // 넘버링 목적, 채팅방을 생성할 때마다 +1
    private int num=0;
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();


    // 1. 채팅방 생성(Admin)
    public void createRoom(ChatRoomDTO chatRoomDTO){
        String id = String.valueOf(num++);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(id);
        chatRoom.setName(chatRoomDTO.getName());

        chatRooms.put(id,chatRoom); // 키가 중복이 되서 발생한 오류로 보인다.

    }

    // 1-1. 채팅방 삭제(Admin)
    // id에 해당하는 chatRoom조회 -> Session연결 강제해지 ->  chatRoom 삭제
    // chatRoom에서 연결이 끊어진 상태에서 새로고침 1번은 상관이 없다.
    // 그런데, 이미 소켓이 연결된 상태에서 새로고침을 할 경우, 클라이언트로부터 여러 세션들이 서버에 중복해서 저장이 된다.
    // 따라서, 재접속이 불가능하다.
    // 결론적으로, 이미 소켓으로 연결이 된 사용자에 대해서는 절대로 새로고침을 하지 못하도록 만들어야 한다.
    public void deleteRoom(String id){
        ChatRoom chatRoom = chatRooms.get(id);
        Set<WebSocketSession> sessions = chatRoom.getSessions();

        List<WebSocketSession> lists = new ArrayList<>(sessions);

        for(WebSocketSession ws:lists){
            System.out.println(ws.getId());
            sessions.remove(ws);
        }

        System.out.println("성공적으로 모든 Session 삭제");

    }

    // 2. 채팅방 리스트 가져오기
    public String getChatRooms()  {
        List<ChatRoomDTO> chatRoomDTO = new ArrayList<>();

        for (String key : chatRooms.keySet()) {
            ChatRoom chatRoom = chatRooms.get(key);

            ChatRoomDTO dao = new ChatRoomDTO();
            dao.setId(chatRoom.getId());
            dao.setName(chatRoom.getName());

            chatRoomDTO.add(dao);
        }

        return new Gson().toJson(chatRoomDTO);

    }

    // 3. 채팅방 가져오기: 채팅방 가져오기 -> Session집합 가져오기 -> 브로드캐스팅으로 채팅 전달하기
    public ChatRoom findRoomById(String id){
        return chatRooms.get(id);
    }
}
