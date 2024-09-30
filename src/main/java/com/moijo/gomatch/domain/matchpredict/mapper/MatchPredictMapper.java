package com.moijo.gomatch.domain.matchpredict.mapper;

import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchPredictMapper {


    List<MatchPredict> selectAllMatchByMember();

    void insertMatchPredict();

    void updateMatchPredict();
}
