package com.moijo.gomatch.domain.chatting.vo;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    private long meetingNo; // 소모임 번호
    @Builder
    public ChatRoom(String roomId, String name, long meetingNo) {
        this.roomId = roomId;
        this.name = name;
        this.meetingNo = meetingNo;
    }
}

