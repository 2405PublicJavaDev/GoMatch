package com.moijo.gomatch.app.matchpredict;

//import com.moijo.gomatch.common.MemberUtils;
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

    /*private final MatchPredictService matchPredictService;

    @GetMapping("/matchPredict")
    public String showMatchPredictionListPage(HttpSession session, Model model) {
        // 승부 예측 목록 조회
        List<MatchPredict> matchPredictions = matchPredictService.getAllMatchByMember();

        // 모델에 승부 예측 목록 추가
        model.addAttribute("matchPredictions", matchPredictions);

        // 뷰 이름 반환 (HTML 템플릿 파일)
        return "matchPredict/matchPredictPage";

    };

    public void addMatchPrediction(){};

    public void modifyMatchPrediction(){}; */
}
