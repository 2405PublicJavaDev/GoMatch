package com.moijo.gomatch.domain.matchpredict.service;


import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;

import java.util.List;

public interface MatchPredictService {
        List<MatchPredict> getAllMatchByMember();
    void addMatchPredict();
    void modifyMatchPredict();

}
