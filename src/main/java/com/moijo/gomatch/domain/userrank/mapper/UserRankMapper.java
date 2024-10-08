package com.moijo.gomatch.domain.userrank.mapper;

import com.moijo.gomatch.domain.userrank.DTO.UserRankDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRankMapper {

//    List<UserRankDTO> selectUserInfo(String memberNickname, String memberId, Long memberExp, Long memberRank);
    void selectRankList();
    void selectWeeklyRankList();
    void selectExperiencePosition();
    void getRankPosition();

}
