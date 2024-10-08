package com.moijo.gomatch.common;

import lombok.Getter;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.function.BiFunction;

@Getter
public class Page<T, S> {
    private final int currentPage; // 현재 페이지 번호
    private final int totalCount; //전체 게시물의 개수
    private final List<T> data;
    private S searchCondition;

    private final int boardLimit; // 한 페이지당 보여줄 게시물의 수
    private final int naviLimit; // 한 페이지당 보여 줄 페이지 번호

    private int maxPage;
    private int startNavi;
    private int endNavi;

    private int prevPage;
    private int nextPage;

    private boolean hasPrev;
    private boolean hasNext;

    private Page(int currentPage, int totalCount, List<T> data, int boardLimit, int naviLimit) {
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.data = data;
        this.boardLimit = boardLimit;
        this.naviLimit = naviLimit;
        makePagination();
    }

    private Page(int currentPage, int totalCount, List<T> data, S searchCondition, int boardLimit, int naviLimit) {
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.data = data;
        this.boardLimit = boardLimit;
        this.naviLimit = naviLimit;
        this.searchCondition = searchCondition;
        makePagination();
    }

    private void makePagination() {
        maxPage = (int)Math.ceil((double) totalCount / boardLimit);
        startNavi = ((currentPage - 1) / naviLimit) * naviLimit + 1;
        endNavi = (startNavi - 1) + naviLimit;
        endNavi = Math.min(endNavi, maxPage);

        prevPage = currentPage - 1;
        nextPage = currentPage + 1;
        hasPrev = currentPage > 1;
        hasNext = currentPage < maxPage;
    }

    public static <T> Page<T, Void> of(int currentPage, int totalCount, BiFunction<Integer, RowBounds,List<T>> function) {
        int boardLimit = 10;
        int naviLimit = 10;
        return of(currentPage, totalCount, function, boardLimit, naviLimit);
    }

    public static <T, S> Page<T, S> of(int currentPage, int totalCount,S searchCondition, TriFunction<Integer,S,RowBounds, List<T>> function) {
        int boardLimit = 10;
        int naviLimit = 10;
        return of(currentPage, totalCount, searchCondition, function, boardLimit, naviLimit);
    }

    public static <T> Page<T, Void> of(int currentPage, int totalCount,BiFunction<Integer, RowBounds,List<T>> function, int boardLimit, int naviLimit) {
        int offset = (currentPage - 1) * boardLimit;
        RowBounds rowBounds = new RowBounds(offset, boardLimit);
        return new Page<>(currentPage, totalCount,function.apply(
                currentPage, rowBounds
        ), boardLimit, naviLimit);
    }

    public static <T, S> Page<T, S> of(int currentPage, int totalCount,S searchCondition, TriFunction<Integer,S,RowBounds, List<T>> function, int boardLimit, int naviLimit) {
        int offset = (currentPage - 1) * boardLimit;
        RowBounds rowBounds = new RowBounds(offset, boardLimit);
        List<T> data = function.apply(
                currentPage, searchCondition, rowBounds
        );
        return new Page<>(currentPage, totalCount, data,searchCondition, boardLimit, naviLimit);
    }

}
