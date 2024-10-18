package com.moijo.gomatch.domain.chatting.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moijo.gomatch.domain.chatting.service.ChatService;
import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    @Override
    public void init() {
        chatRooms = new LinkedHashMap<>();
    }

    @Override
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    @Override
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    @Override
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
}