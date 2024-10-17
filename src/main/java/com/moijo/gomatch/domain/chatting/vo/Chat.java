package com.moijo.gomatch.domain.chatting.vo;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Chat {
    private int chatId;
    private ChatRoom room;  // 방 정보
    private String sender;  // 보낸 사람
    private String senderEmail;  // 보낸 사람 이메일
    private String chatMessage;  // 메시지 내용
    private Timestamp sendDate;  // 보낸 시간
}

