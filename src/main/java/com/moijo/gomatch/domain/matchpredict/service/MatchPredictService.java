package com.moijo.gomatch.domain.matchpredict.service;


import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;

import java.util.List;

public interface MatchPredictService {
    List<MatchPredict> getAllMatchByMember();

    List<MyPredictDTO> getAllMyMatchByMember(String memberId);

    MemberDTO getMemberInfo(String memberId);

    int addMatchPredict(Long gameNo, Long matchPredictNo, String matchPredictDecision, String memberId);

    int modifyMatchPredict(String memberId,Long gameNo,String matchPredictDecision);

    Long getTotalMemberCount();

    double calculatorRankPercent(String memberId);
}
