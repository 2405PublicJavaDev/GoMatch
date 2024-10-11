package com.moijo.gomatch.app.matchpredict;


import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MemberRankDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchPredictController<Prediction> {

    private final MatchPredictService matchPredictService;

    /**
     * 승부 예측 리스트 조회
     *
     * @param
     * @return
     */
    @GetMapping("/matchPredict")
    public String showMatchPredictionListPage(HttpSession session,Model model) {
        // 승부 예측 목록 조회
        String memberId = (String)session.getAttribute("memberId");
        Timestamp gameDate = (Timestamp)session.getAttribute("gameDate");

        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();


        // 모델에 승부 예측 목록 추가
        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank();
        model.addAttribute("matchPredictions", matchPredictions);
        model.addAttribute("memberRank", memberRank);

        if(memberId != null) {
            MemberDTO memberInfo = matchPredictService.getMemberInfo(memberId);
            double rankPercent = matchPredictService.calculatorRankPercent(memberId);

            model.addAttribute("memberInfo", memberInfo);
            model.addAttribute("rankPercent", rankPercent);
        }

        // 뷰 이름 반환 (HTML 템플릿 파일)
        return "matchPredict/matchPredictPage";

    };
    @GetMapping("/matchPredict/list={selectedDate}")
    @ResponseBody
    public ResponseEntity<List<MatchPredict>> getMatchPredictions(@PathVariable String selectedDate) {
        List<MatchPredict> predictions = matchPredictService.getPredictionsByDate(selectedDate);
        return ResponseEntity.ok(predictions); // JSON으로 반환
    }


    /**
     * 나의 예측 리스트 조회(gameNo,memberId),화원 정보 조회
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/myMatchPredict")
    public String showMyMatchPredictionListPage(HttpSession session, Model model) {
        String memberId = (String)session.getAttribute("memberId");
        if(memberId == null) {
            return "redirect:/member/loginpage";
        }

        List<MyPredictDTO> matchPredictions = matchPredictService.getAllMyMatchByMember(memberId);
        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank();
        MemberDTO memberInfo = matchPredictService.getMemberInfo(memberId);
        double rankPercent = matchPredictService.calculatorRankPercent(memberId);

        model.addAttribute("memberRank", memberRank);
        model.addAttribute("matchPredictions", matchPredictions);
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("rankPercent", rankPercent);



        return "matchpredict/myMatchPredictPage";
    }
    /**
     * 예측 등록
     * memberId,MATCH_PREDICT_DECISION
     */
    @PostMapping("/addPredict")
    public String addMatchPrediction(HttpSession session, Model model
            , @RequestParam Long gameNo
            , @RequestParam String matchPredictDecision
            , @RequestParam Long matchPredictNo) {
        String memberId = (String) session.getAttribute("memberId");

        int result = matchPredictService.addMatchPredict(gameNo,matchPredictNo,matchPredictDecision,memberId);


        return "redirect:/matchPredict";
    };

    /**
     * 예측 수정(나의 예측리스트에서 가능)
     * memberId,MATCH_PREDICT_DECISION,MATCH_PREDICT_STATUS
     */
    @PostMapping("/modifyPredict")
    public String modifyMatchPrediction(HttpSession session, Model model
            , @RequestParam Long gameNo
            , @RequestParam String matchPredictDecision){
        String memberId = (String) session.getAttribute("memberId");

        int result = matchPredictService.modifyMatchPredict(memberId,gameNo,matchPredictDecision);

        model.addAttribute("matchPredictDecision", matchPredictDecision);

        return "redirect:/myMatchPredict";
    };

    @GetMapping("/matchPredictions/date")
    @ResponseBody
    public ResponseEntity<List<MatchPredict>> getPredictionsByDate(@RequestParam String date) {
        // 데이터 조회 로직
        List<MatchPredict> predictions = List.of();
        return ResponseEntity.ok(predictions);
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
}
