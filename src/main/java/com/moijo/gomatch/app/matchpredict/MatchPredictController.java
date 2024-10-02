package com.moijo.gomatch.app.matchpredict;

<<<<<<< HEAD
=======
//import com.moijo.gomatch.common.MemberUtils;
>>>>>>> 9c5882bf6b837b85ee1111ec6bfe20de086c04c3
import com.moijo.gomatch.domain.matchpredict.service.MatchPredictService;
import com.moijo.gomatch.domain.matchpredict.vo.MatchPredict;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchPredictController {

    private final MatchPredictService matchPredictService;

    /**
     * 승부 예측 리스트 조회
     * @param model
     * @return
     */
    @GetMapping("/matchPredict")
    public String showMatchPredictionListPage(HttpSession session, Model model) {
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
        // String memberId = session.getAttribute("memberId").toString();
        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();
        return "matchPredict/myMatchPredictPage";
    }
    /**
     * 예측 등록
     * memberId,MATCH_PREDICT_DECISION
     */
    public void addMatchPrediction(){};

    /**
     * 예측 수정(나의 예측리스트에서 가능)
     * memberId,MATCH_PREDICT_DECISION,MATCH_PREDICT_STATUS
     */
    public void modifyMatchPrediction(){};
}
