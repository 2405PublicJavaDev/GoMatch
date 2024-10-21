package com.moijo.gomatch.domain.meeting.service.impl;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardMapper;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardLikeVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingBoardServiceImpl implements MeetingBoardService {

    private final MeetingBoardMapper meetingBoardMapper;

    // ■■■■■■■■■■■■■■■■■■■ 나의 소모임 (MyMeeting) ■■■■■■■■■■■■■■■■■■■■ //
    // 내가 작성한 게시글 리스트 불러오기
    @Override
    public List<MeetingBoardVO> getBoardsByMemberId(String memberId) {
        return meetingBoardMapper.selectBoardsByMemberId(memberId);
    }

    // 내가 작성한 댓글 리스트 불러오기
    @Override
    public List<MeetingBoardReplyVO> getBoardRepliesByMemberId(String memberId) {
        return meetingBoardMapper.selectBoardRepliesByMemberId(memberId);
    }

    // 내가 좋아요 한 게시글 리스트 불러오기
    @Override
    public List<MeetingBoardLikeVO> getBoardLikesByMemberId(String memberId) {
        return meetingBoardMapper.selectBoardLikesByMemberId(memberId);
    }
    // ■■■■■■■■■■■■■■■■■■■ 게시글 조회 및 검색 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 상세 정보 조회
    @Override
    public MeetingBoardVO getBoardDetail(long meetingBoardNo) {
        return meetingBoardMapper.selectBoardDetail(meetingBoardNo);
    }

    // 게시글 파일 리스트 조회
    @Override
    public List<MeetingBoardFileVO> getMeetingBoardFiles(long meetingBoardNo) {
        return meetingBoardMapper.getFilesByMeetingBoardNo(meetingBoardNo);
    }

    // 검색 조건에 맞는 게시글 수 조회
    @Override
    public int getBoardCount(String filterType, String searchType, String keyword) {
        return meetingBoardMapper.selectBoardCount(filterType, searchType, keyword);
    }

    // 검색 조건에 맞는 게시글 리스트 조회
    @Override
    public List<MeetingBoardVO> getBoardList(int page, int pageSize, String filterType, String searchType, String keyword) {
        int offset = (page - 1) * pageSize;
        return meetingBoardMapper.selectBoardList(offset, pageSize, filterType, searchType, keyword);
    }

    // 모든 게시글 리스트 조회
    @Override
    public List<MeetingBoardVO> getAllBoards() {
        return meetingBoardMapper.selectAllBoards();
    }

    // 이전 게시글 번호 조회
    @Override
    public Long getPreviousPostId(long meetingBoardNo) {
        return meetingBoardMapper.selectPreviousPostId(meetingBoardNo);
    }

    // 다음 게시글 번호 조회
    @Override
    public Long getNextPostId(long meetingBoardNo) {
        return meetingBoardMapper.selectNextPostId(meetingBoardNo);
    }

    // 게시글 좋아요 수 조회
    @Override
    public int getLikeCount(long meetingBoardNo) {
        return meetingBoardMapper.selectLikeCount(meetingBoardNo);
    }

    // 특정 게시글의 댓글 리스트 조회
    @Override
    public List<MeetingBoardReplyVO> getRepliesByBoardId(long meetingBoardNo) {
        return meetingBoardMapper.selectRepliesByBoardId(meetingBoardNo);
    }
    // ■■■■■■■■■■■■■■■■■■■ 게시글 등록/수정/삭제 (CRUD) ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 등록
    @Override
    public void addBoard(MeetingBoardVO meetingBoardVO) {
        meetingBoardMapper.insertBoard(meetingBoardVO);
    }

    // 게시글 삭제
    @Override
    public void removeBoard(long meetingBoardNo) {
        meetingBoardMapper.deleteBoard(meetingBoardNo);
    }

    // 게시글 수정
    @Override
    public void modifyBoard(MeetingBoardVO meetingBoardVO) {
        meetingBoardMapper.updateBoard(meetingBoardVO);
    }

    // 게시글 파일 삭제
    @Override
    public void deleteBoardFiles(List<Long> fileDeleteIds) {
        log.info("MeetingBoardServiceImpl - 삭제할 파일 ID 목록: {}", fileDeleteIds);
        if (fileDeleteIds != null && !fileDeleteIds.isEmpty()) {
            meetingBoardMapper.deleteBoardFiles(fileDeleteIds);
        }
    }

    // 게시글 조회수 증가
    @Override
    public void increaseViewCount(long meetingBoardNo) {
        meetingBoardMapper.updateMeetingBoardViewCount(meetingBoardNo);
    }

    // ■■■■■■■■■■■■■■■■■■■ 게시글 좋아요 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글 좋아요 처리
    @Override
    public void likePost(long meetingBoardNo, String memberId) {
        meetingBoardMapper.insertLike(meetingBoardNo, memberId);
    }

    // 게시글 좋아요 취소 처리
    @Override
    public void unlikePost(long meetingBoardNo, String memberId) {
        meetingBoardMapper.deleteLike(meetingBoardNo, memberId);
    }

    // 게시글 좋아요 여부 확인
    @Override
    public boolean checkLikeStatus(long meetingBoardNo, String memberId) {
        return meetingBoardMapper.checkLikeStatus(meetingBoardNo, memberId) > 0;
    }

    // ■■■■■■■■■■■■■■■■■■■ 게시글 댓글 ■■■■■■■■■■■■■■■■■■■■ //
    // 게시글에 댓글 등록
    @Override
    public boolean addReply(long meetingBoardNo, String memberId, String meetingReplyContent) {
        return meetingBoardMapper.insertReply(meetingBoardNo, memberId, meetingReplyContent) > 0;
    }

    // 게시글 댓글 삭제
    @Override
    public boolean deleteReply(long meetingReplyNo) {
        return meetingBoardMapper.deleteReply(meetingReplyNo) > 0;
    }
}