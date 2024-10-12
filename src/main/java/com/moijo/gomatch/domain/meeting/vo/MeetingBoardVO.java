package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MeetingBoardVO {
    private Long meetingBoardNo;
    private String meetingBoardTitle;
    private String meetingBoardContent;
    private String meetingBoardCategory;
    private String memberId;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

}
