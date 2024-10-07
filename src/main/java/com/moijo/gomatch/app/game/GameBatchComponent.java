package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.service.GameService;
import com.moijo.gomatch.domain.game.vo.GameVO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
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

    // KBO 팀 리그 순위 가져오기
    public List<String[]> scrapeTeamRank(String yearParam) {
        List<String[]> teamList = new ArrayList<>();  // String 배열 리스트로 데이터 저장
        try {
            Document doc = Jsoup.connect("https://sports.news.naver.com/kbaseball/record/index.nhn?category=kbo&year=" + yearParam)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                    .get();
            Elements baseballTeams = doc.select("#regularTeamRecordList_table > tr");
            for (Element baseballTeam : baseballTeams) {
                Element rank = baseballTeam.selectFirst("th"); // 등 수
                Element title = baseballTeam.selectFirst("span:nth-child(2)"); // 팀 명
                Element match = baseballTeam.selectFirst("td:nth-child(3)"); // 경기 수
                Element victory = baseballTeam.selectFirst("td:nth-child(4)"); // 승
                Element defeat = baseballTeam.selectFirst("td:nth-child(5)"); // 패
                Element draw = baseballTeam.selectFirst("td:nth-child(6)"); // 무
                Element rate = baseballTeam.selectFirst("td:nth-child(7)"); // 승률
                Element winning = baseballTeam.selectFirst("td:nth-child(9)"); // 연승
                Element recent = baseballTeam.selectFirst("td:nth-child(12)"); // 최근 10경기
                // Element logo = baseballTeam.selectFirst("td.tm  img"); // 팀 로고
                String logoUrl = gameService.getLogoUrl(title.text());
                if (title != null) {
                    String[] teamData = {
                            rank.text(), title.text(), match.text(), victory.text(), defeat.text(),
                            draw.text(), rate.text(), winning.text(), recent.text(), logoUrl
                    };  // 팀의 정보들을 문자열 배열로 저장한다
                    teamList.add(teamData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamList;
    }

    // 선수 순위 가져오기 - 투수
    public List<String[]> scrapePitcherRank(String yearParam) {
        List<String[]> pitcherList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/driver/chromedriver.exe");
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        // 브라우저 창 숨기는 옵션 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Headless 모드
        options.addArguments("--disable-gpu");  // GPU 비활성화
        options.addArguments("--no-sandbox");  // Sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");  // 메모리 이슈 방지
        options.addArguments("--window-size=1920,1080");  // 창 크기 설정
        options.addArguments("--remote-allow-origins=*"); // 모든 오리진 허용

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://sports.daum.net/record/kbo/pitcher?season=" +yearParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tbl_record")));
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements baseballTeams = doc.select(".tbl_record > tbody > tr");
            for (Element baseballTeam : baseballTeams) {
                Element rank = baseballTeam.selectFirst("td.td_rank"); // 등 수
                Element name = baseballTeam.selectFirst(".td_name .txt_name"); // 선수이름
                Element team = baseballTeam.selectFirst(".td_team a"); // 팀 명
                Element games = baseballTeam.selectFirst("td[data-field='pitGp']"); // 경기 수
                Element victory = baseballTeam.selectFirst("td[data-field='pitW']"); // 승
                Element defeat = baseballTeam.selectFirst("td[data-field='pitL']"); // 패
                Element save = baseballTeam.selectFirst("td[data-field='pitSv']"); // 세이브
                Element holds = baseballTeam.selectFirst("td[data-field='pitHld']"); // 홀드
                Element innings = baseballTeam.selectFirst("td[data-field='pitIp2']"); // 이닝
                Element pitches = baseballTeam.selectFirst("td[data-field='pitNp']");   // 투구수
                Element hitsAllowed = baseballTeam.selectFirst("td[data-field='pitH']");  // 피안타
                Element homeRunsAllowed = baseballTeam.selectFirst("td[data-field='pitHr']"); // 피홈런
                Element strikeouts = baseballTeam.selectFirst("td[data-field='pitSo']"); // 탈삼진
                Element walksHitsAllowed = baseballTeam.selectFirst("td[data-field='pitBbhp']"); // 사사구
                Element loss = baseballTeam.selectFirst("td[data-field='pitR']");  // 실점
                Element earnedRuns = baseballTeam.selectFirst("td[data-field='pitEr']"); // 자책
                Element era = baseballTeam.selectFirst("td[data-field='pitEra']"); // 평균자책
                Element whip = baseballTeam.selectFirst("td[data-field='pitWhip']"); // WHIP
                Element qs = baseballTeam.selectFirst("td[data-field='pitQs']"); // QS

                if (name != null) {
                    String[] pitcherData = {
                            rank.text(), name.text(), team.text(), games.text(), victory.text(), defeat.text(),
                            save.text(), holds.text(), innings.text(), pitches.text(), hitsAllowed.text(),
                            homeRunsAllowed.text(), strikeouts.text(), walksHitsAllowed.text(), loss.text(),
                            earnedRuns.text(), era.text(), whip.text(), qs.text()
                    };  // 팀의 정보들을 문자열 배열로 저장한다
                    pitcherList.add(pitcherData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();  // 크롤링이 끝난 후 브라우저를 닫음
        }
        return pitcherList;
    }


    // 야구 경기 리그 일정 가져오기
    public List<GameVO> scrapeSchedule(String dateParam) {
        List<GameVO> gameList = new ArrayList<>();
        // Selenium WebDriver 설정 (ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/driver/chromedriver.exe");

        // 브라우저 창 숨기는 옵션 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--window-size=1920,1080");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);
        try {
            // 셀레니움으로 다음 스포츠 홈페이지 크롤링
            driver.get("https://sports.daum.net/schedule/kbo?date=" + dateParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
    private int parseScore(String score) {
        if (score != null && !score.trim().isEmpty() && !score.equals("-")) {
            try {
                return Integer.parseInt(score);
            } catch (NumberFormatException e) {
                System.err.println("Invalid score format: " + score);
            }
        }
        return 0; // 기본값은 0으로 설정
    }

     // @Scheduled 어노테이션을 통해 매일 9:30에 실행
     @Scheduled(cron = "0 30 09 * * ?")
    public void updateGameSchedule() {
        String dateParam = "202410";    // 10월의 경기를 스케줄러로 자동 저장
        List<GameVO> gameList = scrapeSchedule(dateParam);
        if (!gameList.isEmpty()) { // 경기 정보가 있는 경우에만 저장
            gameService.saveAllGames(gameList);
            System.out.println("경기 등록은 완료했습니다.");
        } else {
            System.out.println("저장할 경기 정보가 없습니다."); // 로그 출력
        }
    }
}
