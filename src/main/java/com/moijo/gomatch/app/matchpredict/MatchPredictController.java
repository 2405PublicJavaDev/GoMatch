package com.moijo.gomatch.app.matchpredict;


import com.moijo.gomatch.domain.matchpredict.dto.MyPredictDTO;
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showMatchPredictionListPage(Model model) {
        // 승부 예측 목록 조회
        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();

        // 모델에 승부 예측 목록 추가
        model.addAttribute("matchPredictions", matchPredictions);


        // 뷰 이름 반환 (HTML 템플릿 파일)
        return "matchPredict/matchPredictPage";

    };
    /**
     * 나의 예측 리스트 조회(gameNo,memberId)
      * @param session
     * @param model
     * @return
     */
    @GetMapping("/myMatchPredict")
    public String showMyMatchPredictionListPage(HttpSession session, Model model) {
//        memberId = session.getAttribute("memberId").toString();
        String memberId = "user1";

        List<MyPredictDTO> matchPredictions = matchPredictService.getAllMyMatchByMember(memberId);

        model.addAttribute("matchPredictions", matchPredictions);
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
//        String memberId = (String) session.getAttribute("memberId");
        String memberId = "user1";

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
        String memberId = "user1";

        int result = matchPredictService.modifyMatchPredict(memberId,gameNo,matchPredictDecision);

        model.addAttribute("matchPredictDecision", matchPredictDecision);

        return "redirect:/myMatchPredict";
    };
}
