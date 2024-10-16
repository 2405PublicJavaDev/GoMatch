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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchPredictController {

    private final MatchPredictService matchPredictService;

    /**
     * 승부 예측 리스트 조회
     *
     * @param
     * @return
     */
    @GetMapping("/matchPredict")
    public String showMatchPredictionListPage(HttpSession session, Model model
            ,@RequestParam(value = "gameNo", required = false) Long gameNo) {
        // 승부 예측 목록 조회
        String memberId = (String)session.getAttribute("memberId");
        boolean hasPrediction = false; // 예측 상태 변수 초기화
        log.info("Session memberId: {}", memberId);
        Timestamp gameDate = (Timestamp)session.getAttribute("gameDate");
        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();
        if (memberId != null) {

            MemberDTO memberInfo = matchPredictService.getMemberInfo(memberId);
            log.info("MemberInfo: {}", memberInfo);
            if (memberInfo != null) {
                model.addAttribute("memberInfo", memberInfo);
                double rankPercent = matchPredictService.calculatorRankPercent(memberId);
                model.addAttribute("rankPercent", rankPercent);
            }
        }

        // 모델에 승부 예측 목록 추가
        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank();
        model.addAttribute("matchPredictions", matchPredictions);
        model.addAttribute("memberRank", memberRank);


        // 뷰 이름 반환 (HTML 템플릿 파일)
        return "matchPredict/matchPredictPage";
    }

    @GetMapping("/matchPredict/list/{gameDate}")
    @ResponseBody
    public ResponseEntity<List<MatchPredict>> getMatchPredictions(@PathVariable String gameDate) {
        List<MatchPredict> predictions = matchPredictService.getPredictionsByDate(gameDate);
        return ResponseEntity.ok(predictions); // JSON으로 반환
    }

//    @GetMapping("/rankingList/{startDate}/{endDate}")
//    @ResponseBody
//    public ResponseEntity<List<MemberRankDTO>> getAllRankingList(@PathVariable String startDate, @PathVariable String endDate) {
//        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank(startDate,endDate);
//        return ResponseEntity.ok(memberRank);
//    }

//    function fetchRankingList(startDate,endDate){
//        fetch(`/rankingList/${startDate}/${endDate}`)
//                .then(response => {
//        if(!response.ok) {
//            throw new Error(`Http error! status: ${response.status}`);
//        }
//        return response.json();
//                })
//                .then(data => {
//                updateRankingList(data);
//                })
//                .catch(error => console.error(`Error fetching ranking list:`,error));
//    }


    /**
     * 나의 예측 리스트 조회(gameNo,memberId),화원 정보 조회
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/myMatchPredict")
    public String showMyMatchPredictionListPage(HttpSession session, Model model
    ,@RequestParam(required = false) Long gameNo) {
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
            , @RequestParam Long matchPredictNo, RedirectAttributes redirectAttributes) {

        String memberId = (String) session.getAttribute("memberId");

        if (memberId == null) {
            return "redirect:/member/loginpage"; // 로그인하지 않은 경우
        }


        if(matchPredictService.hasPredictionForGame(memberId, gameNo)) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 이 경기에 대한 예측을 등록 하였습니다.");
            return "redirect:/matchPredict";
        }

        try {
            int result = matchPredictService.addMatchPredict(gameNo, matchPredictNo, matchPredictDecision, memberId);

            if (result > 0) {
//                int addRank = matchPredictService.addMemberRanking(memberId);
                return "redirect:/matchPredict"; // 성공적으로 추가된 경우
            } else {
                model.addAttribute("errorMessage", "예측 등록에 실패했습니다."); // 실패 메시지 추가
            }
        } catch (Exception e) {
            log.error("Error adding match prediction: {}", e.getMessage());
            model.addAttribute("errorMessage", "예측 등록 중 오류가 발생했습니다.");
        }
        return "redirect:/matchPredict"; // 에러 발생 시 같은 페이지로
    }


    @PostMapping("/matchPedict")
    public int increaseExperience(HttpSession session, Model model
    , @RequestParam Long gameNo){
        String memberId = (String) session.getAttribute("memberId");

        int result = matchPredictService.increaseExperience(memberId,gameNo);
        return result;
    }

    /**
     * 예측 수정(나의 예측리스트에서 가능)
     * memberId,MATCH_PREDICT_DECISION,MATCH_PREDICT_STATUS
     */
    @PostMapping("/modifyPredict")
    public String modifyMatchPrediction(HttpSession session, Model model
            , @RequestParam Long gameNo
            , @RequestParam String matchPredictDecision) {
        String memberId = (String) session.getAttribute("memberId");

        int result = matchPredictService.modifyMatchPredict(memberId, gameNo, matchPredictDecision);

        // 예측 수정 후 gameResult를 통해 예측이 맞았는지 확인
        matchPredictService.increaseExperience(memberId, gameNo); // 경험치 증가 호출

        model.addAttribute("matchPredictDecision", matchPredictDecision);

        return "redirect:/myMatchPredict";
    }

    /**
     * 유저 랭킹 등록
     * @param model
     * @param session
     */
    @PostMapping



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
