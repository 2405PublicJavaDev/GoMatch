package com.moijo.gomatch.sample.service;

import com.moijo.gomatch.common.Page;
import com.moijo.gomatch.sample.domain.Sample;
import com.moijo.gomatch.sample.dto.SampleSearchCondition;
import com.moijo.gomatch.sample.dto.SampleWriteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SampleService {
    List<Sample> selectAll();

    Page<Sample, Void> selectPage(int currentPage);

    Page<Sample, SampleSearchCondition> selectPageSearch(int currentPage, SampleSearchCondition searchCondition);

    int insertSample(SampleWriteDTO dto, List<MultipartFile> uploadFile) throws IOException;
}
