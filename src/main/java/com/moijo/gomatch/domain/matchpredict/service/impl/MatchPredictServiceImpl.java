package com.moijo.gomatch.domain.matchpredict.service.impl;

import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.mapper.MatchPredictMapper;
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchPredictServiceImpl implements MatchPredictService {
    private final MatchPredictMapper matchPredictMapper;

    /**
     * 승부예측 리스트 조회
     *
     * @return
     */
    @Override
    public List<MatchPredict> getAllMatchByMember() {
        return matchPredictMapper.selectAllMatchByMember();
    }

    /**
     * 나의 예측 리스트 조회
     * @param memberId
     * @return
     */
    @Override
    public List<MyPredictDTO> getAllMyMatchByMember(String memberId) {
        return matchPredictMapper.selectAllMyMatchByMember(memberId);
    }

    /**
     * 예측 등록
     *
     * @param gameNo
     * @param matchPredictNo
     * @param matchPredictDecision
     * @param memberId
     * @return
     */
    @Override
    public int addMatchPredict(Long gameNo, Long matchPredictNo, String matchPredictDecision, String memberId) {
        int result = matchPredictMapper.insertMatchPredict(gameNo,matchPredictDecision,memberId,matchPredictNo);
        return result;
    }

    /**
     * 예측 수정
     *
     * @param memberId
     * @return
     */
    @Override
    public int modifyMatchPredict(String memberId,Long gameNo,String matchPredictDecision) {
        int result = matchPredictMapper.updateMatchPredict(memberId,gameNo,matchPredictDecision);
        return result;
    }
}
