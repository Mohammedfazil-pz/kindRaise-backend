package com.example.KindRaise.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.dtos.ChatRequestDto;
import com.example.KindRaise.service.ChatService;

@RestController
@RequestMapping("/api/genai")
public class ChatBotController {

    @Autowired
    ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequestDto chatRequestDto){
        String reply=this.chatService.getReply(chatRequestDto.getMessage(),chatRequestDto.getUserId());
        return ResponseEntity.ok(Map.of("reply",reply));
    }
}
