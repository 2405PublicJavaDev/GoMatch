package com.moijo.gomatch.common;

// 패키지 정의: 이 클래스는 'com.moijo.gomatch.common' 패키지에 포함됩니다.
//
//주요 기능:
//파일 업로드:
//
//파일을 지정된 디렉토리에 업로드하고, 업로드된 파일 정보를 데이터베이스에 저장합니다.
//파일이 저장될 폴더가 존재하지 않으면 자동으로 생성합니다.
//파일 삭제:
//
//특정 파일을 실제 디스크에서 삭제하고, 관련 정보를 데이터베이스에서 제거합니다.
//파일명 변경:
//
//파일 업로드 시 고유한 이름을 생성하여 중복을 방지합니다.

import com.moijo.gomatch.sample.domain.SampleFile;
import com.moijo.gomatch.sample.repository.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.moijo.gomatch.common.FileConfig.FOLDER_PATH;

// 필요한 클래스와 라이브러리들을 임포트합니다.
// `FileConfig.FOLDER_PATH`를 사용하여 파일 업로드 경로를 참조합니다.

@Component
@Slf4j
@RequiredArgsConstructor
// Spring의 `@Component` 애노테이션으로 이 클래스를 빈으로 등록합니다.
// `@Slf4j`는 Lombok의 애노테이션으로 로그를 편리하게 사용하도록 합니다.
// `@RequiredArgsConstructor`는 모든 final 필드에 대해 생성자를 자동 생성합니다.

public class FileUtil {
    private final SampleMapper sampleMapper;
    // 이 클래스는 여러 Mapper를 사용하여 파일 관련 데이터를 처리합니다.

    /**
     * 파일을 업로드하고 업로드한 파일 정보를 DB 에 저장합니다.
     *
     * @param uploadCategory: upload 할 파일 종류
     * @param files:          upload 할 파일 list
     * @param fkNo:           foreign key id
     * @return int: db에 저장된 row 개수
     * @throws IOException: 파일 저장에 오류가 발생할 때
     */
    public int uploadFiles(UploadCategory uploadCategory, List<MultipartFile> files, Long fkNo) throws IOException {
        // 파일을 업로드하고 DB에 저장하는 로직입니다.
        int result = 0;
        long order = 1;
        for (MultipartFile file : files) {
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
                String fileName = file.getOriginalFilename();
                String fileRename = fileRename(fileName);
                // 파일명을 변경하기 위해 `fileRename` 메서드를 사용합니다.
                File folder = new File(FileConfig.realFolderPath + uploadCategory.toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                // 파일이 저장될 디렉토리를 생성합니다. 디렉토리가 없으면 새로 만듭니다.
                String filePath = FileConfig.realFolderPath + uploadCategory + "/" + fileRename;
                file.transferTo(new File(folder, fileRename));
                // 파일을 지정된 위치에 저장합니다.
                result += insertFileData(uploadCategory, fkNo, fileName, fileRename, filePath, order);
                // 파일 정보를 데이터베이스에 저장합니다.
                order++;
            }
        }
        return result;
    }

    /**
     * 파일을 삭제하고 DB 에도 파일 관련된 정보를 삭제할 때 쓰입니다.
     *
     * @param uploadCategory: 삭제할 파일 종류
     * @param fkNo:           삭제할 foreign key id
     * @return int: db에 삭제된 row 개수
     * @throws IOException: 파일 삭제에 오류가 발생할 때
     */
    public int deleteFiles(UploadCategory uploadCategory, Long fkNo) throws IOException {
        // 파일을 삭제하고 DB에서 관련 정보를 삭제하는 로직입니다.
        List<?> files = selectFileData(uploadCategory, fkNo);
        if (deleteFilesReal(uploadCategory, files)) {
            throw new IOException("file not delete completely");
        }
        return deleteFilesData(uploadCategory, fkNo);
    }

    // DB에 파일 정보를 저장하는 로직입니다.
    private int insertFileData(UploadCategory uploadCategory, Long fkNo, String fileName, String fileRename, String filePath, Long order) {
        int result = 0;
        switch (uploadCategory) {
            case SAMPLE -> {
                SampleFile sampleFile = new SampleFile();
                sampleFile.setFileName(fileName);
                sampleFile.setFileRename(fileRename);
                sampleFile.setFilePath(filePath);
                sampleFile.setSampleNo(fkNo);
                result += sampleMapper.insertSampleFile(sampleFile);
            }
        }
        return result;
    }

    // 업로드된 파일의 데이터를 조회하는 메서드입니다.
    private List<?> selectFileData(UploadCategory uploadCategory, Long fkNo) {
        switch (uploadCategory) {
            case SAMPLE -> {
                return sampleMapper.selectAllSampleFiles(fkNo);
            }
        }
        return Collections.emptyList();
    }

    // 실제 파일을 삭제하는 로직입니다.
    private boolean deleteFilesReal(UploadCategory uploadCategory, List<?> files) {
        switch (uploadCategory) {
            case SAMPLE -> {
                boolean success = true;
                for (Object file : files) {
                    SampleFile sampleFile = (SampleFile) file;
                    success &= new File(sampleFile.getFilePath()).delete();
                }
                return success;
            }
        }
        return false;
    }

    // DB에서 파일 정보를 삭제하는 메서드입니다.
    private int deleteFilesData(UploadCategory uploadCategory, Long fkNo) {
        int result = 0;
        switch (uploadCategory) {
            case SAMPLE -> {
                result += sampleMapper.deleteSampleFile(fkNo);
            }
        }
        return result;
    }

    // 파일명을 고유하게 변경하기 위한 메서드입니다.
    private static String fileRename(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileRename = UUID.randomUUID() + ext;
        return fileRename;
    }
}
