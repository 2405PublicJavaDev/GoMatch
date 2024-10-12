package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MeetingBoardFileVO {
    private Long meetingBoardFileNo;
    private String fileName;
    private String fileRename;
    private String filePath;
    private int fileOrder;
    private int meetingBoardNo;
    private String webPath;
    private Timestamp regDate;
    private Timestamp updateDate;
}
