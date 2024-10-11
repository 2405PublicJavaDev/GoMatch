package com.moijo.gomatch.domain.matchpredict.service;


import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MemberRankDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface MatchPredictService {
    /**
     * 승부 예측 리스트
     * @return
     */
    List<MatchPredict> getAllMatchByMember();

    /**
     * 날짜 별 승부예측 리스트
     * @param selectedDate
     * @return
     */
    List<MatchPredict> getPredictionsByDate(String selectedDate);

    /**
     * 나의 예측 리스트
     * @param memberId
     * @return
     */
    List<MyPredictDTO> getAllMyMatchByMember(String memberId);

    /**
     * 전체 순위
     * @return
     */
    List<MemberRankDTO> getAllMemberRank();

    /**
     * 회원 정보
     * @param memberId
     * @return
     */
    MemberDTO getMemberInfo(String memberId);

    /**
     * 예측 등록
     * @param gameNo
     * @param matchPredictNo
     * @param matchPredictDecision
     * @param memberId
     * @return
     */
    int addMatchPredict(Long gameNo, Long matchPredictNo, String matchPredictDecision, String memberId);

    /**
     * 예측 수정
     * @param memberId
     * @param gameNo
     * @param matchPredictDecision
     * @return
     */
    int modifyMatchPredict(String memberId,Long gameNo,String matchPredictDecision);

    /**
     * 전체 회원
     * @return
     */
    Long getTotalMemberCount();

    /**
     * 순위 퍼센트
     * @param memberId
     * @return
     */
    double calculatorRankPercent(String memberId);



}
