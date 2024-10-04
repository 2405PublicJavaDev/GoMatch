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

    @GetMapping("/game/rank")
    // 팀 순위 보여주는 메소드
    public String showRankPage(@RequestParam(value = "year", required = false,  defaultValue = "2024") String year, Model model) {
        List<String[]> teamList = gameBatchComponent.scrapeRank(year);
        model.addAttribute("teams", teamList);
        model.addAttribute("selectedYear", year);
        return "game/rankPage";
    }

    // 경기 일정 보여주는 메소드
    @GetMapping("/game/list")
    public String showGameListPage(@RequestParam(value = "month", required = false, defaultValue = "10") String month, Model model) {
        String year = "2024";
        String dateParam = year + month;
        // 크롤링 한 데이터로부터 gameList 리스트 생성
        List<GameVO> gameList = gameBatchComponent.scrapeSchedule(dateParam);
        model.addAttribute("games", gameList);  // gameList를 games라는 이름으로 페이지에 저장
        model.addAttribute("selectedMonth", month);
        return "game/listPage";
    }
}
