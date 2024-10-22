package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;
import com.moijo.gomatch.domain.game.vo.StadiumVO;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameBatchComponent gameBatchComponent;

    @Autowired
    private GameService gameService;

    /**
     * 담당자: 홍예은
     * 관련 기능: [Login Check] 로그인 여부 확인
     * 설명: 현재 세션에 저장된 멤버 아이디를 확인하여 로그인 여부를 JSON으로 반환
     */
    @GetMapping("/game/checkLogin")
    @ResponseBody
    public Map<String, Boolean> checkLogin(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        response.put("loggedIn", memberId != null);
        return response;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 연도별 KBO 팀 리그 순위 출력
     * 설명: 크롤링 한 KBO 팀 리그 순위 출력
     */
    @GetMapping("/game/teamrank")
    public String showTeamRankPage(@RequestParam(value = "year", required = false, defaultValue = "2024") String year, Model model, HttpSession session) {
        List<String[]> teamList = gameBatchComponent.scrapeTeamRank(year);
        model.addAttribute("teams", teamList);
        model.addAttribute("selectedYear", year);
        return "game/teamRankPage";
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 연도별 KBO 선수 순위 출력
     * 설명: 크롤링 한 KBO 선수 순위 (투수, 타자) 출력
     */
    @GetMapping("/game/playerrank")
    public String showPlayerRankPage(
            @RequestParam(value = "year", required = false, defaultValue = "2024") String year,
            @RequestParam(value = "type", required = false, defaultValue = "pitcher") String type,
            Model model) {

        List<String[]> pitcherList = gameBatchComponent.scrapePitcherRank(year);
        List<String[]> batterList = gameBatchComponent.scrapeBatterRank(year);

        model.addAttribute("pitchers", pitcherList);
        model.addAttribute("batters", batterList);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedYear", year);

        return "game/playerRankPage";
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 2024년 KBO 경기 일정 출력
     * 설명: 크롤링 한 KBO 월 별 경기 일정 출력
     */
    @GetMapping("/game/list")
    public String showGameListPage(HttpSession session, @RequestParam(value = "month", required = false, defaultValue = "10") String month, Model model) {
        String preferenceClub = (String) session.getAttribute("preferenceClub");
        String year = "2024";
        String dateParam = year + month;
        // 크롤링 한 데이터로부터 gameList 리스트 생성
        List<GameVO> gameList = gameBatchComponent.scrapeSchedule(dateParam);
        model.addAttribute("games", gameList);  // gameList를 games라는 이름으로 페이지에 저장
        model.addAttribute("selectedMonth", month);
        model.addAttribute("preferenceClub", preferenceClub);
        return "game/listPage";
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 야구장 별 날씨 출력
     * 설명: 야구장의 날씨정보(실시간, 시간대별, 주간대별) 출력
     */
    @GetMapping("/game/weather")
    public String getStadiums(Model model) {
        List<StadiumVO> stadiumList = gameService.getAllStadiums();
        model.addAttribute("stadiums", stadiumList);
        return "game/weatherPage";
    }
}
