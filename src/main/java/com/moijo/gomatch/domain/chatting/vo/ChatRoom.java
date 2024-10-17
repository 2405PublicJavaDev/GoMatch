package com.moijo.gomatch.domain.chatting.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {
    private int chatRoomId;  // 방 ID
    private String chatRoomName;  // 방 이름
}

