package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MeetingBoardReplyVO {
    private int meetingReplyNo;
    private String meetingReplyContent;
    private int meetingBoardNo;
    private String memberId;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String memberNickName;
}
