package com.moijo.gomatch.domain.userrank.service.impl;

import com.moijo.gomatch.domain.userrank.DTO.UserRankDTO;
import com.moijo.gomatch.domain.userrank.mapper.UserRankMapper;
import com.moijo.gomatch.domain.userrank.service.UserRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRankServiceImpl implements UserRankService {
    private final UserRankMapper userRankMapper;


//    @Override
//    public List<UserRankDTO> getUserInfo(String memberNickname, String memberId, Long memberExp, Long memberRank) {
//        return userRankMapper.selectUserInfo(memberNickname,memberId,memberExp,memberRank);
//    }

    @Override
    public void getRankList() {

    }

    @Override
    public void getWeeklyRankList() {

    }

    @Override
    public void getExperiencePosition() {

    }

    @Override
    public void getRankPosition() {

    }
}
