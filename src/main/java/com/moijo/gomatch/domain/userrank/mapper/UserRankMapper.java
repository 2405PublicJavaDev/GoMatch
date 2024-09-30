package com.moijo.gomatch.domain.userrank.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRankMapper {

    void selectRankList();
    void selectWeeklyRankList();
    void selectExperiencePosition();
    void getRankPosition();
}
