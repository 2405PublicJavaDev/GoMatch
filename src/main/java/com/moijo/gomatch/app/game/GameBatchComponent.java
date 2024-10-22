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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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

    /**
     * 담당자: 홍예은
     * 관련 기능: [Crawling] 연도별 KBO 팀 리그 순위 가져오기
     * 설명: 다음 스포츠 크롤링하여 KBO 팀 리그 순위 가져오기
     */
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

    /**
     * 담당자: 홍예은
     * 관련 기능: [Crawling] 연도별 KBO 투수 선수 순위 가져오기
     * 설명: 다음 스포츠 크롤링하여 KBO 투수 선수 순위 가져오기
     */
    public List<String[]> scrapePitcherRank(String yearParam) {
        List<String[]> pitcherList = new ArrayList<>();

        // 브라우저 창 숨기는 옵션 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Headless 모드
        options.addArguments("--disable-gpu");  // GPU 비활성화
        options.addArguments("--no-sandbox");  // Sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");  // 메모리 이슈 방지
        options.addArguments("--window-size=1920,1080");  // 창 크기 설정
        options.addArguments("--remote-allow-origins=*"); // 모든 오리진 허용

        // pageLoadStrategy 설정 추가
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("pageLoadStrategy", "none");  // 빠른 페이지 로딩
        options.merge(caps);

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://sports.daum.net/record/kbo/pitcher?season=" + yearParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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
        } catch(TimeoutException e) {
            System.err.println("페이지 로드가 시간 내 완료되지 않았습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();  // 크롤링이 끝난 후 브라우저를 닫음
        }
        return pitcherList;
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Crawling] 연도별 KBO 타자 선수 순위
     * 설명: 다음 스포츠 크롤링하여 KBO 타자 선수 순위
     */
    public List<String[]> scrapeBatterRank(String yearParam) {
        List<String[]> batterList = new ArrayList<>();

        // 브라우저 창 숨기는 옵션 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Headless 모드
        options.addArguments("--disable-gpu");  // GPU 비활성화
        options.addArguments("--no-sandbox");  // Sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");  // 메모리 이슈 방지
        options.addArguments("--window-size=1920,1080");  // 창 크기 설정
        options.addArguments("--remote-allow-origins=*"); // 모든 오리진 허용

        // pageLoadStrategy 설정 추가
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("pageLoadStrategy", "none");  // 빠른 페이지 로딩
        options.merge(caps);

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://sports.daum.net/record/kbo/batter?season=" +yearParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tbl_record")));
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements baseballTeams = doc.select(".tbl_record > tbody > tr");
            for (Element baseballTeam : baseballTeams) {
                Element rank = baseballTeam.selectFirst("td.td_rank"); // 등 수
                Element name = baseballTeam.selectFirst(".td_name .txt_name"); // 선수이름
                Element team = baseballTeam.selectFirst(".td_team a"); // 팀 명
                Element games = baseballTeam.selectFirst("td[data-field='batGp']"); // 경기 수
                Element batPa = baseballTeam.selectFirst("td[data-field='batPa']"); // 타석
                Element batAb = baseballTeam.selectFirst("td[data-field='batAb']"); // 타수
                Element batH = baseballTeam.selectFirst("td[data-field='batH']"); // 안타
                Element bat2b = baseballTeam.selectFirst("td[data-field='bat2b']"); // 2타
                Element bat3b = baseballTeam.selectFirst("td[data-field='bat3b']"); // 3타
                Element batHr = baseballTeam.selectFirst("td[data-field='batHr']");   // 홈런
                Element batRbi = baseballTeam.selectFirst("td[data-field='batRbi']");  // 타점
                Element batR = baseballTeam.selectFirst("td[data-field='batR']"); // 득점
                Element batSb = baseballTeam.selectFirst("td[data-field='batSb']"); // 도루
                Element batBbhp = baseballTeam.selectFirst("td[data-field='batBbhp']"); // 사사구
                Element batSo = baseballTeam.selectFirst("td[data-field='batSo']");  // 삼진
                Element batAvg = baseballTeam.selectFirst("td[data-field='batAvg']"); // 타율
                Element batObp = baseballTeam.selectFirst("td[data-field='batObp']"); // 출루율
                Element batSlg = baseballTeam.selectFirst("td[data-field='batSlg']"); // 장타율
                Element batOps = baseballTeam.selectFirst("td[data-field='batOps']"); // OPS

                if (name != null) {
                    String[] batterData = {
                            rank.text(), name.text(), team.text(), games.text(), batPa.text(), batAb.text(),
                            batH.text(), bat2b.text(), bat3b.text(), batHr.text(), batRbi.text(),
                            batR.text(), batSb.text(), batBbhp.text(), batSo.text(),
                            batAvg.text(), batObp.text(), batSlg.text(), batOps.text()
                    };  // 팀의 정보들을 문자열 배열로 저장한다
                    batterList.add(batterData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();  // 크롤링이 끝난 후 브라우저를 닫음
        }
        return batterList;
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Crawling] 2024년 KBO 경기 일정
     * 설명: 다음 스포츠 크롤링하여 2024년 KBO 경기 일정/결과 출력
     */
    public List<GameVO> scrapeSchedule(String dateParam) {
        List<GameVO> gameList = new ArrayList<>();
        // Selenium WebDriver 설정 (ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/driver/chromedriver.exe");

        // 브라우저 창 숨기는 옵션 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Headless 모드
        options.addArguments("--disable-gpu");  // GPU 비활성화
        options.addArguments("--no-sandbox");  // Sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");  // 메모리 이슈 방지
        options.addArguments("--window-size=1920,1080");  // 창 크기 설정
        options.addArguments("--remote-allow-origins=*"); // 모든 오리진 허용

        // pageLoadStrategy 설정 추가
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("pageLoadStrategy", "none");  // 빠른 페이지 로딩
        options.merge(caps);

        WebDriver driver = new ChromeDriver(options);
        try {
            // 셀레니움으로 다음 스포츠 홈페이지 크롤링
            driver.get("https://sports.daum.net/schedule/kbo?date=" + dateParam);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
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

                    // 게임 상태 추출
                    String gameStatus = team.selectFirst("span.state_game").text();

                    if(!"경기취소".equals(gameStatus)) {
                        gameStatus = null;
                    }
                    GameVO game = new GameVO(sqlDate, gameTime, homeTeamName, awayTeamName, gameLocation, homeScore, awayScore);
                    game.setGameStatus(gameStatus);
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

    /**
     * 담당자: 홍예은
     * 관련 기능: [MOD] 점수 형변환
     * 설명: String으로 크롤링 된 점수를 Integer로 형변환
     */
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

    /**
     * 담당자: 홍예은
     * 관련 기능: [Update] 경기 정보 업데이트
     * 설명: 스케줄러 어노테이션을 통해 9:30마다 경기 정보 db 업뎃
     */
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
