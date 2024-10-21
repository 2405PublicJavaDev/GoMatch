package com.moijo.gomatch.domain.chatting.service;

import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import jakarta.annotation.PostConstruct;

import java.util.List;

public interface ChatService {

    @PostConstruct
    void init();

    List<ChatRoom> findAllRoom(long meetingNo);

    ChatRoom findRoomById(String roomId);

    ChatRoom createRoom(String name, long meetingNo);
}
