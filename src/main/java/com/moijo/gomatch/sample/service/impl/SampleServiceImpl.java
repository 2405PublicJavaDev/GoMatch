package com.moijo.gomatch.sample.service.impl;

import com.moijo.gomatch.common.FileUtil;
import com.moijo.gomatch.common.Page;
import com.moijo.gomatch.common.UploadCategory;
import com.moijo.gomatch.sample.domain.Sample;
import com.moijo.gomatch.sample.dto.SampleSearchCondition;
import com.moijo.gomatch.sample.dto.SampleWriteDTO;
import com.moijo.gomatch.sample.repository.SampleMapper;
import com.moijo.gomatch.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {
    private final SampleMapper sampleMapper;
    private final FileUtil fileUtil;
    @Override
    public List<Sample> selectAll() {
        return sampleMapper.selectAll();
    }

    @Override
    public Page<Sample, Void> selectPage(int currentPage) {
        return Page.of(currentPage, sampleMapper.selectPageAllCount(), sampleMapper::selectPageAll);
    }

    @Override
    public Page<Sample, SampleSearchCondition> selectPageSearch(int currentPage, SampleSearchCondition searchCondition) {
        return Page.of(currentPage, sampleMapper.selectPageConditionCount(searchCondition), searchCondition, sampleMapper::selectPageCondition);
    }

    @Override
    public int insertSample(SampleWriteDTO dto, List<MultipartFile> uploadFiles) throws IOException {
        fileUtil.uploadFiles(UploadCategory.SAMPLE, uploadFiles, 1L);
        return 0;
    }
}
