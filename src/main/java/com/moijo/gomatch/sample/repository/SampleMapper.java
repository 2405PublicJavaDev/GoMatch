package com.moijo.gomatch.sample.repository;

import com.moijo.gomatch.sample.domain.Sample;
import com.moijo.gomatch.sample.domain.SampleFile;
import com.moijo.gomatch.sample.dto.SampleSearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface SampleMapper {
    List<Sample> selectAll();

    List<Sample> selectPageAll(int currentPage, RowBounds rowBounds);

    List<Sample> selectPageCondition(int currentPage, SampleSearchCondition searchCondition, RowBounds rowBounds);

    int selectPageAllCount();

    int selectPageConditionCount(SampleSearchCondition searchCondition);

    int insertSampleFile(SampleFile sampleFile);

    List<SampleFile> selectAllSampleFiles(Long sampleNo);

    int deleteSampleFile(Long sampleNo);
}
