package com.moijo.gomatch.domain.chatting.service;

import com.moijo.gomatch.domain.chatting.vo.ChatMessage;
import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {

    @PostConstruct
    void init();

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomById(String roomId);

    ChatRoom createRoom(String name);
}
