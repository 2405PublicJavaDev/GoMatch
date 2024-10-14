package com.moijo.gomatch.domain.matchpredict.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRankMapper {
    int getUserExperience(String memberId);

    void updateUserExperience(String memberId, int newExperience);
}
