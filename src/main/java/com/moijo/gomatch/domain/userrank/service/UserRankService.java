package com.moijo.gomatch.domain.userrank.service;

import com.moijo.gomatch.domain.userrank.DTO.UserRankDTO;

import java.util.List;

public interface UserRankService {

//    List<UserRankDTO> getUserInfo(String memberNickname, String memberId, Long memberExp, Long memberRank);

    void getRankList();
    void getWeeklyRankList();
    void getExperiencePosition();
    void getRankPosition();

}
