package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MeetingBoardLikeVO {
    private long meetingBoardLikeNo;
    private String meetingBoardLikeYn;
    private long meetingBoardNo;
    private String memberId;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String meetingBoardTitle;
}
