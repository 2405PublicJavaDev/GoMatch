package com.moijo.gomatch.domain.matchpredict.service.impl;

import com.moijo.gomatch.domain.matchpredict.mapper.UserRankMapper;
import com.moijo.gomatch.domain.matchpredict.service.UserRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UseRankrServiceImpl implements UserRankService {

    private final UserRankMapper userRankMapper;

    // 사용자 경험치 가져오기
    public int getUserExperience(String memberId) {
        return userRankMapper.getUserExperience(memberId); // Mapper에서 경험치를 조회합니다.
    }

    // 사용자 경험치 업데이트
    public void updateUserExperience(String memberId, int newExperience) {
        userRankMapper.updateUserExperience(memberId, newExperience); // Mapper에서 경험치를 업데이트합니다.
    }
}
