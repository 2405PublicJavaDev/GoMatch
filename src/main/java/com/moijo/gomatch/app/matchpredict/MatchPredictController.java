package com.moijo.gomatch.app.matchpredict;


import com.moijo.gomatch.domain.matchpredict.dto.MemberDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MemberRankDTO;
import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import com.moijo.gomatch.domain.matchpredict.vo.PredictionRequest;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            , @RequestParam(value = "gameNo", required = false) Long gameNo) {
        String memberId = (String)session.getAttribute("memberId");
        boolean hasPrediction = false; // 예측 상태 변수 초기화
        log.info("Session memberId: {}", memberId);
//        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();
        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank(); // 멤버 랭킹 리스트 조회
        if (memberId != null) {

            MemberDTO memberInfo = matchPredictService.getMemberInfo(memberId);
            log.info("MemberInfo: {}", memberInfo);
            if (memberInfo != null) {
                model.addAttribute("memberInfo", memberInfo);
                double rankPercent = matchPredictService.calculatorRankPercent(memberId);
                model.addAttribute("rankPercent", rankPercent);
            }
        }

//        model.addAttribute("matchPredictions", matchPredictions);
        model.addAttribute("memberRank", memberRank);

        return "matchPredict/matchPredictPage";
    }

    @GetMapping("/matchPredict/list/{gameDate}")
    @ResponseBody
    public ResponseEntity<List<MatchPredict>> getMatchPredictions(HttpSession session
            ,@PathVariable String gameDate) {
        String memberId = (String)session.getAttribute("memberId");
        List<MatchPredict> predictions = matchPredictService.getPredictionsByDate(gameDate,memberId);
        return ResponseEntity.ok(predictions); // JSON으로 반환
    }


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
        List<MemberRankDTO> memberRank = matchPredictService.getAllMemberRank(); // 멤버 랭킹 리스트 조회
        MemberDTO memberInfo = matchPredictService.getMemberInfo(memberId); // 내 정보 조회
        double rankPercent = matchPredictService.calculatorRankPercent(memberId); // 퍼센트 조회

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
    @ResponseBody
    public ResponseEntity<?> addMatchPrediction(@RequestBody PredictionRequest request, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        if (matchPredictService.hasPredictionForGame(memberId, request.getGameNo())) {
            return ResponseEntity.badRequest().body("이미 이 경기에 대한 예측을 등록하였습니다.");
        }

        try {
            int result = matchPredictService.addMatchPredict(request.getGameNo(), request.getMatchPredictNo(), request.getMatchPredictDecision(), memberId);
            if (result > 0) {
                matchPredictService.updateRank();
                return ResponseEntity.ok("예측이 성공적으로 등록되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("예측 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예측 등록 중 오류가 발생했습니다.");
        }
    }
    @PostMapping("updateMemberRank")
    public String updateMemberRank(HttpSession session, Model model) {
        try{
        int result = matchPredictService.updateRank();
        } catch (Exception e) {
            log.error("Error updating member rank: {}", e.getMessage());
            model.addAttribute("errorMessage", "랭킹 업데이트 오류 발생");
        }
        return "redirect:/matchPredict";
    }


    /**
     * 경험치 추가
      */

    @PostMapping("/matchPredict")
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
        matchPredictService.updateRank();
        // 예측 수정 후 gameResult를 통해 예측이 맞았는지 확인
        matchPredictService.increaseExperience(memberId, gameNo); // 경험치 증가 호출

        model.addAttribute("matchPredictDecision", matchPredictDecision);

        return "redirect:/myMatchPredict";
    }

    /**
     * 유저 랭킹 등록
     * @param
     * @param session
     */
//    @PostMapping("updateUserRank")
//    public String updateUserRank(){
//
//        return
//    }

    @GetMapping("/matchPredict/memberId")
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



}
