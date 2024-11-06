package com.example.demo.controller;

import com.example.demo.dto.ChatRoomDTO;
import com.example.demo.service.ChatRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
public class AdminController {

    private final ChatRoomService chatRoomService;

    // JSON 타입으로 데이터를 받을 것으로 판단.
    @PostMapping("/chat/create")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO){
        chatRoomService.createRoom(chatRoomDTO);

        return ResponseEntity.ok("Make Room successfully");
    }

    @DeleteMapping("/chat/room/{id}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable int id){
        chatRoomService.deleteRoom(String.valueOf(id));

        return ResponseEntity.ok("Successfully Delete Room");
    }
}
