package com.moijo.gomatch.domain.chatting.vo;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoom {
    private String roomId; // 방 번호
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    private long meetingNo; // 소모임 번호
    private LocalDateTime createdDate;

    @Builder
    public ChatRoom(String roomId, String name, long meetingNo, LocalDateTime createdDate) {
        this.roomId = roomId;
        this.name = name;
        this.meetingNo = meetingNo;
        this.createdDate = LocalDateTime.now();
    }
}

