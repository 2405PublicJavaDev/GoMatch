package com.moijo.gomatch.app.matchpredict;

import com.moijo.gomatch.domain.userrank.DTO.UserRankDTO;
import com.moijo.gomatch.domain.userrank.service.UserRankService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserRankController {

    private final UserRankService userRankService;

//    @GetMapping("/userInfo")
//    public String getUserInfo(HttpSession session
//    , Model model
//    , @RequestParam("memberNickname")String memberNickname
//    , @RequestParam("memberId") String memberId
//    , @RequestParam("memberExp")Long memberExp
//    , @RequestParam("memberRank")Long memberRank) {
//            memberId = (String)session.getAttribute("memberId").toString();
//        List<UserRankDTO>userInfo = userRankService.getUserInfo(memberNickname,memberId,memberExp,memberRank);
//
//        if(memberId != null) {
//            model.addAttribute("userInfo", userInfo);
//            return"matchpredict/myMatchPredictPage";
//        }
//        return "redirect:/login";
//    }

    public void getRankList(){};

    public void getWeeklyRankList(){};

    public void getExperiencePosition(){};

    public void getRankPosition(){};
}
