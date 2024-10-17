package com.moijo.gomatch.domain.chatting.mapper;

import com.moijo.gomatch.domain.chatting.vo.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    void insertChat(Chat chat);  // XML에 정의된 쿼리를 실행

    List<Chat> getChatsByRoomId(@Param("roomId") String room);  // 방 ID로 채팅 목록 조회
}
