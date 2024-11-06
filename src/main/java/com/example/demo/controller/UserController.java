package com.example.demo.controller;

import com.example.demo.service.ChatRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
public class UserController {

    // 생성자 주입을 통해서 컴파일시 오류를 캐치할 수 있음
    private final ChatRoomService chatRoomService;

    // 데모 버전이라서 일단 와일드 카드로, 아무거나 넣을 수 있도록 했음. 하지만 추후 세부화되면 와읻르 카드 빼고 수정할 예정임.
    @GetMapping("/chat/room/list")
    public ResponseEntity<?> getChatRooms(){
        String chatRoomList = chatRoomService.getChatRooms();
        return ResponseEntity.ok(chatRoomList);
    }

}
