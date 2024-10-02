package com.moijo.gomatch.domain.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class MemberFile {

    private int fileNo;
    private String fileName;
    private String fileRename;
    private String filePath;
    private String userId;

    private MultipartFile uploadFile;
}
