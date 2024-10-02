package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class GameBatchComponent {

    private GameService gameService;

    public GameBatchComponent() {
    }

    @Autowired
    public GameBatchComponent(GameService gameService) {
        this.gameService = gameService;
    }

    // 야구 경기 리그 일정 가져오기
    public List<GameVO> scrapeSchedule(String dateParam) {
        List<GameVO> gameList = new ArrayList<>();
        // Selenium WebDriver 설정 (ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/driver/chromedriver.exe");

        // 브라우저 창 숨기는 옵션 추가하기
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            // 셀레니움으로 다음 스포츠 홈페이지 크롤링
            driver.get("https://sports.daum.net/schedule/kbo?date=" + dateParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("scheduleList")));
            // 셀레니움으로 가져온 HTML을 Jsoup으로 파싱
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements baseballSchedule = doc.select("#scheduleList > tr");

            for (Element Schedule : baseballSchedule) {
                String day = Schedule.attr("data-date"); // 경기날짜
                Element time = Schedule.selectFirst("td.td_time");  // 경기시간
                Element location = Schedule.selectFirst("td.td_area");  // 경기장 위치
                Element team = Schedule.selectFirst("td.td_team");  // 경기 팀
                // 날짜 텍스트 추출 (05.01 같은 형식)
                java.sql.Date sqlDate = null; // 초기화
                // data-date를 LocalDate로 변환
                LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));
                sqlDate = Date.valueOf(localDate); // java.sql.Date로 변환
                // 시간 텍스트 추출
                String gameTime = (time != null) ? time.text() : "";

                // 위치 텍스트 추출
                String gameLocation = (location != null) ? location.text() : "";

                if (team != null) {
                    // 홈 팀과 원정 팀 이름 추출
                    String homeTeamName = team.selectFirst("div.info_team.team_home .txt_team").text();
                    String awayTeamName = team.selectFirst("div.info_team.team_away .txt_team").text();

                    // 홈 팀과 원정 팀 점수 추출
                    String homeTeamScore = team.selectFirst("div.info_team.team_home .num_score").text();
                    String awayTeamScore = team.selectFirst("div.info_team.team_away .num_score").text();

                    // 점수 문자열이 유효한 숫자인지 확인하는 메서드 호출
                    int homeScore = parseScore(homeTeamScore);
                    int awayScore = parseScore(awayTeamScore);

                    GameVO game = new GameVO(sqlDate, gameTime, homeTeamName, awayTeamName, gameLocation, homeScore, awayScore);
                    gameList.add(game);
                    }
            }
        } catch (Exception e) {
            System.err.println("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
        return gameList;
    }

    // 점수를 int로 파싱
    private Integer parseScore(String score) {
        if (score != null && !score.trim().isEmpty() && !score.equals("-")) {
            try {
                return Integer.parseInt(score);
            } catch (NumberFormatException e) {
                System.err.println("Invalid score format: " + score);
            }
        }
        return null; // 기본값 설정
    }

     // @Scheduled 어노테이션을 통해 매일 10:00에 실행
     @Scheduled(cron = "0 0 10 * * ?")
    public void updateGameSchedule() {
        String dateParam = "202410";    // 10월의 경기를 스케줄러로 자동 저장
        List<GameVO> gameList = scrapeSchedule(dateParam);
        if (!gameList.isEmpty()) { // 경기 정보가 있는 경우에만 저장
            gameService.saveAllGames(gameList);
        } else {
            System.out.println("저장할 경기 정보가 없습니다."); // 로그 출력
        }
    }
}
