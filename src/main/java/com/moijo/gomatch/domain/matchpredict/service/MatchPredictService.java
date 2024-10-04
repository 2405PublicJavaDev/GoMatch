package com.moijo.gomatch.domain.matchpredict.service;


import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;

import java.util.List;

public interface MatchPredictService {
    List<MatchPredict> getAllMatchByMember();

//    List<MyMatchPredict> getAllMyMatchByMember(String memberId, Long gameNo);

    int addMatchPredict(Long gameNo, String matchPredictDecision);

    void modifyMatchPredict();

}
