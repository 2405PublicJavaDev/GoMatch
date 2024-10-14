package com.moijo.gomatch.domain.matchpredict.service;

public interface UserRankService {

    int getUserExperience(String memberId);

    void updateUserExperience(String memberId, int newExperience);
}
