package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.game.vo.StadiumVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GameController {

    @Autowired
    private GameBatchComponent gameBatchComponent;

    @Autowired
    private GameService gameService;

    // 팀 순위 보여주는 메소드
    @GetMapping("/game/teamrank")
    public String showTeamRankPage(@RequestParam(value = "year", required = false,  defaultValue = "2024") String year, Model model) {
        List<String[]> teamList = gameBatchComponent.scrapeTeamRank(year);
        model.addAttribute("teams", teamList);
        model.addAttribute("selectedYear", year);
        return "game/teamRankPage";
    }

    // 선수 순위 보여주는 메소드
    @GetMapping("/game/playerrank")
    public String showPlayerRankPage(@RequestParam(value = "year", required = false, defaultValue = "2024") String year, Model model) {
        List<String[]> pitcherList = gameBatchComponent.scrapePitcherRank(year);
        model.addAttribute("pitchers", pitcherList);
        model.addAttribute("selectedYear", year);
        return "game/playerRankPage";
    }

    // 경기 일정 보여주는 메소드
    @GetMapping("/game/list")
    public String showGameListPage(HttpSession session, @RequestParam(value = "month", required = false, defaultValue = "10") String month, Model model) {
        //String memberId = (String) session.getAttribute("memberId");
        String preferenceClub = (String) session.getAttribute("preferenceClub");
        String year = "2024";
        String dateParam = year + month;
        // 크롤링 한 데이터로부터 gameList 리스트 생성
        List<GameVO> gameList = gameBatchComponent.scrapeSchedule(dateParam);
        model.addAttribute("games", gameList);  // gameList를 games라는 이름으로 페이지에 저장
        model.addAttribute("selectedMonth", month);
        //model.addAttribute("memberId", memberId);
        model.addAttribute("preferenceClub", preferenceClub);
        return "game/listPage";
    }

    // 야구장 날씨 보여주는 메소드
    @GetMapping("/game/weather")
    public String getStadiums(Model model) {
        List<StadiumVO> stadiumList = gameService.getAllStadiums();
        model.addAttribute("stadiums", stadiumList);
        return "game/weatherPage";
    }
}
