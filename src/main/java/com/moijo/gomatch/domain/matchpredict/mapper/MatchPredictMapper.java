package com.moijo.gomatch.domain.matchpredict.mapper;

import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchPredictMapper {

    /**
     * 승부예측 리스트 조회
     * @return
     */
    List<MatchPredict> selectAllMatchByMember();

    void insertMatchPredict();

    void updateMatchPredict();
}
