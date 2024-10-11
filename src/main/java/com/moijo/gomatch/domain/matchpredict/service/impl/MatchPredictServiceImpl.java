package com.moijo.gomatch.domain.matchpredict.service.impl;

import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MemberRankDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.mapper.MatchPredictMapper;
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
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
     *
     * @param selectedDate
     * @return
     */
    @Override
    public List<MatchPredict> getPredictionsByDate(String selectedDate) {
        return matchPredictMapper.selectPredictByDate(selectedDate);
    }

    /**
     * 회원 순위 리스트 조회
     * @return
     */
    @Override
    public List<MemberRankDTO> getAllMemberRank() {
        return matchPredictMapper.selectAllMemberRank();
    }

    /**
     * 나의 예측 리스트 조회
     * @param memberId
     * @return
     */
    @Override
    public List<MyPredictDTO> getAllMyMatchByMember(String memberId) {
        List<MyPredictDTO> myPredictDTO = matchPredictMapper.selectAllMyMatchByMember(memberId);

        if (myPredictDTO != null) {
            return myPredictDTO;
        }
        return null;
    }


    /**
     * 회원 정보
     * @param memberId
     * @return
     */
    @Override
    public MemberDTO getMemberInfo(String memberId) {
        MemberDTO memberDTO = (MemberDTO) matchPredictMapper.selectMemberInfo(memberId);
        return  memberDTO;
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

    /**
     * 전체 회원
     * @return
     */
    @Override
    public Long getTotalMemberCount() {
        return matchPredictMapper.getTotalMemberCount();
    }

    /**
     * 순위 퍼센트
     * @param memberId
     * @return
     */
    @Override
    public double calculatorRankPercent(String memberId) {
        Long totalMember = getTotalMemberCount(); // 전체 회원 정보

        Long memberRank = matchPredictMapper.getOneByMemberRank(memberId);

        if(memberRank != null) {
            double percent =  (double) memberRank / (double)totalMember * 100;

            String formattedPercent = String.format("%.2f", percent);

            return Double.parseDouble(formattedPercent);

        }
        return 0.0;
    }



}
