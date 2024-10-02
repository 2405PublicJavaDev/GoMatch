package com.moijo.gomatch.app.game;

import com.moijo.gomatch.domain.game.vo.GameVO;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
public class GameBatchComponent {

    // KBO 리그 일정 가져오기
    public List<GameVO> scrapeSchedule(String month) {
        List<GameVO> gameList = new ArrayList<>();
        // Selenium WebDriver 설정 (ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/driver/chromedriver.exe");

        // 브라우저 창 숨기는 옵션 추가하기
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            // 셀레니움으로 KBO 홈페이지 크롤링
            driver.get("https://www.koreabaseball.com/Schedule/Schedule.aspx");

            // AJAX 요청을 통해 월별 데이터를 가져오는 코드 작성
            String requestBody = "leId=1&srIdList=0,9,6&seasonId=2024&gameMonth=" + month + "&teamId=";

            // POST 요청 보내기
            // Document doc = Jsoup.parse(driver.getPageSource());
            String jsonResponse = Jsoup.connect("https://www.koreabaseball.com/ws/Schedule.asmx/GetScheduleList")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .requestBody(requestBody)
                    .post()
                    .body()
                    .text();  // JSON 응답을 문자열로 변환

            // JSON 응답 파싱
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray rows = jsonObject.getJSONArray("rows");

            // JSON 데이터에서 경기 정보 추출
            for (int i = 0; i < rows.length(); i++) {
                JSONObject rowObject = rows.getJSONObject(i);
                JSONArray row = rowObject.getJSONArray("row");

                String gameDate = row.getJSONObject(0).getString("Text");
                String gameTime = row.getJSONObject(1).getString("Text");
                String playDetails = row.getJSONObject(2).getString("Text");
                String gameField = row.getJSONObject(7).getString("Text");

                // 팀 정보 파싱
                String[] teams = extractTeamsFromPlay(playDetails);

                GameVO game = new GameVO(gameDate, gameTime, teams[0], "vs", teams[1], gameField);
                gameList.add(game); // 경기 리스트에 추가
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
        return gameList;
    }

    // 팀 정보를 파싱하는 메소드
    private String[] extractTeamsFromPlay(String playDetails) {
        playDetails = playDetails.replaceAll("<[^>]+>", "").trim();  // HTML 태그 제거
        String[] teams = playDetails.split("vs");
        return new String[]{teams[0].trim(), teams[1].trim()};
    }
}
//            Elements baseballSchedule = doc.select("#tblScheduleList > tbody > tr");
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd(EEE)"); // 날짜 포맷 지정
//            String currentDay = null;
//
//            for (Element Schedule : baseballSchedule) {
//                Element gameDate = Schedule.selectFirst("td.day");  // 경기날짜
//                Element gameTime = Schedule.selectFirst("td.time"); // 경기시간
//                Element team_A = Schedule.selectFirst("td.play > span");    // 팀1
//                Element vs = Schedule.selectFirst("td.play > em");  // VS
//                Element team_B = Schedule.selectFirst("td.play > span:nth-child(3)"); // 팀2
//                Element gameField = Schedule.selectFirst("td:nth-child(8)"); // 경기장소
//
//                // 경기장소가 -로 되어있으면 td 의 자식요소 7번째를 받아옴
//                if ("-".equals(gameField.text())) {
//                    gameField = Schedule.selectFirst("td:nth-child(7)");
//                }
//
//                // 경기날짜가 null 이 아니면 gameDate의 text를 받아옴
//                if (gameDate != null) {
//                    if (currentDay == null || !currentDay.equals(gameDate.text())) {
//                        currentDay = gameDate.text();
//                    }
//                }
//
//                // 경기시간이 null 이 아니면 해당 경기 Game 객체 생성
//                if (gameTime != null) {
//                    GameVO Game = new GameVO(currentDay, gameTime.text(), team_A.text(), vs.text(), team_B.text(),
//                            gameField.text());
//                    gameList.add(Game); // 경기리스트에 추가
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // WebDriver 종료
//            driver.quit();
//        }
//        return gameList;
//    }
//}
