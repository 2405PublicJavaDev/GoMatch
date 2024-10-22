package com.moijo.gomatch.domain.chatting.service;

import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import jakarta.annotation.PostConstruct;

import java.util.List;

public interface ChatService {

    @PostConstruct
    void init();

    /**
     * 모든 채팅방 출력
     * @param meetingNo
     * @return List<ChatRoom>
     */
    List<ChatRoom> findAllRoom(long meetingNo);

    /**
     * 해당 채팅방 입장
     * @param roomId
     * @return ChatRoom
     */
    ChatRoom findRoomById(String roomId);

    /**
     * 채팅방 생성
     * @param name
     * @param meetingNo
     * @return ChatRoom
     */
    ChatRoom createRoom(String name, long meetingNo);
}
