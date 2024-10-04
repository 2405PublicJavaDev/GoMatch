package com.moijo.gomatch.domain.matchpredict.service.impl;

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

//    @Override
//    public List<MyMatchPredict> getAllMyMatchByMember(String memberId, Long gameNo) {
//        return matchPredictMapper.selectAllMyMatchByMember(memberId,gameNo);
//    }


    @Override
    public int addMatchPredict(Long gameNo,String matchPredictDecision) {
        int result = matchPredictMapper.insertMatchPredict(gameNo,matchPredictDecision);
        System.out.println(gameNo);
        return result;
    }

    @Override
    public void modifyMatchPredict() {

    }
}
