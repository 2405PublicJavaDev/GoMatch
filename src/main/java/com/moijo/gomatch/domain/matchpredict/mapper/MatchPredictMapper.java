package com.moijo.gomatch.domain.matchpredict.mapper;

import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MemberRankDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface MatchPredictMapper {

    /**
     * 승부예측 리스트 조회
     * @return
     */
    List<MatchPredict> selectAllMatchByMember();

    /**
     * 날짜 별 승부예측 리스트
     * @param selectedDate
     * @return
     */
    List<MatchPredict> selectPredictByDate(String selectedDate);
    

    /**
     * 회원 순윌 리스트 조회
     * @return
     */
    List<MemberRankDTO> selectAllMemberRank();

    /**
     * 나의 예측 리스트 조회
     * @param memberId
     * @return
     */
    List<MyPredictDTO> selectAllMyMatchByMember(String memberId);

    /**
     * 회원 정보 조회
     * @param memberId
     * @return
     */
    MemberDTO selectMemberInfo(String memberId);

    /**
     * 전체 회원 조회
     * @return
     */
    Long getTotalMemberCount();

    /**
     * 회원 순위 조회
     * @return
     */
    Long getOneByMemberRank(String memberId);

    /**
     * 예측 등록
     * @param gameNo
     * @param matchPredictDecision
     * @param memberId
     * @param matchPredictNo
     * @return
     */
    int insertMatchPredict(Long gameNo, String matchPredictDecision,String memberId,Long matchPredictNo);

    /**
     * 예측 수정
     * @param memberId
     * @param gameNo
     * @param matchPredictDecision
     * @return
     */
    int updateMatchPredict(String memberId,Long gameNo,String matchPredictDecision);


}
