package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.domain.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

//    @GetMapping("/meeting")
//    public String showMeetings(Model model
//            , @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage) {
//        model.addAttribute("page", meetingService.getAllMeetings(currentPage, condition));
//        return "meeting";
//    }

}
