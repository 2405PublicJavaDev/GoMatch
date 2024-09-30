package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingRestController {
    private final MeetingService meetingService;

//    @GetMapping("/meeting")
//    public String showMeetings(Model model
//            , @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage) {
//        model.addAttribute("page", meetingService.getAllMeetings(currentPage, condition));
//        return "meeting";
//    }

}
