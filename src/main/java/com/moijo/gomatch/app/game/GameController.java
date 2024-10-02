package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.vo.GameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class GameController {

    @Autowired
    private GameBatchComponent gameBatchComponent;

    // 경기 일정 보여주는 메소드
    @GetMapping("/game/list")
    public String showGameListPage(@RequestParam(value = "month", required = false) String month, Model model) {
        String year = "2024";
        if (month == null || !month.matches("0[1-9]|1[0-2]")) {
            // 유효하지 않은 경우 기본값 설정 (예: 현재 월)
            month = "10"; // 기본값을 10월로 설정
        }
        String dateParam = year + month;
        // 크롤링 한 데이터로부터 gameList 리스트 생성
        List<GameVO> gameList = gameBatchComponent.scrapeSchedule(dateParam);
        model.addAttribute("games", gameList);  // gameList를 games라는 이름으로 페이지에 저장
        return "game/listPage";
    }
}
