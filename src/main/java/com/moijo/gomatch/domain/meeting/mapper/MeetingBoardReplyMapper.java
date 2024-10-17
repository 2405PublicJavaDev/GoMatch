package com.moijo.gomatch.domain.meeting.mapper;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingBoardReplyMapper {
    // 게시글 등록 메서드
    void insertBoard(MeetingBoardVO mBoardVO);
}
