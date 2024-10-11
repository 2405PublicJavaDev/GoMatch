package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MeetingBoardVO {
    private int meetingBoardNo;
    private String meetingBoardTitle;
    private String meetingBoardContent;
    private String meetingBoardCategory;
    private String memberId;
    private Timestamp regDate;
    private Timestamp updateDate;

}
