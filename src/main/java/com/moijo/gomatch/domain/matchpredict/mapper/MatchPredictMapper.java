package com.moijo.gomatch.domain.matchpredict.mapper;

import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
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

//    List<MyMatchPredict> selectAllMyMatchByMember(String memberId, Long gameNo);

    int insertMatchPredict(Long gameNo, String matchPredictDecision);

    void updateMatchPredict();

}
