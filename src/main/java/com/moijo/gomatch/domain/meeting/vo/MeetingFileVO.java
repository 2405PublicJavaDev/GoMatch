package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MeetingFileVO {
    private int meetingFileNo;
    private String fileName;
    private String fileRename;
    private String filePath;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String webPath;
    private int fileOrder;
    private int meetingNo;
}
