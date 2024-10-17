package com.moijo.gomatch.app.admin;

import com.moijo.gomatch.domain.admin.service.AdminMemberService;
import com.moijo.gomatch.domain.admin.vo.AdminMeetingVO;
import com.moijo.gomatch.domain.member.service.MemberService;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminService;


    @GetMapping("/admin/admin-mainpage")
    public String showAdminMainPage(Model model) {
        List<MemberVO> recentMembers = adminService.getRecentMembers();
        List<AdminMeetingVO> recentMeetings = adminService.getRecentMeetings();
        model.addAttribute("members", recentMembers);
        model.addAttribute("meetings", recentMeetings);
        return "admin/admin-mainpage";
    }

    @GetMapping("/admin/admin-member")
    public String showAdminMemberPage(Model model) {
        List<MemberVO> members = adminService.getAllMembers();
        model.addAttribute("members", members);
        return "admin/admin-member";
    }


    // 관리자 회원 정지/ 삭제 기능 (함수)
    @PostMapping("/admin/suspend-member")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> suspendMember(@RequestBody Map<String, String> payload) {
        String memberId = payload.get("memberId");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean suspended = adminService.suspendMember(memberId);
            response.put("success", suspended);
            response.put("message", suspended ? "회원이 정지되었습니다." : "회원 정지에 실패했습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류 발생: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/activate-member")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> activateMember(@RequestBody Map<String, String> payload) {
        String memberId = payload.get("memberId");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean activated = adminService.activateMember(memberId);
            response.put("success", activated);
            response.put("message", activated ? "회원이 활성화되었습니다." : "회원 활성화에 실패했습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류 발생: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/delete-member")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMember(@RequestBody Map<String, String> payload) {
        String memberId = payload.get("memberId");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = adminService.deleteMemberByAdmin(memberId);
            response.put("success", deleted);
            response.put("message", deleted ? "회원이 삭제되었습니다." : "회원 삭제에 실패했습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류 발생: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    // 관리자 회원 검색
    @GetMapping("/admin/search-members")
    public String searchMembers(@RequestParam(required = false) String searchType,
                                @RequestParam(required = false) String searchKeyword,
                                Model model) {
        List<MemberVO> members;

        if (StringUtils.hasText(searchKeyword)) {
            switch (searchType) {
                case "id":
                    members = adminService.searchMembersById(searchKeyword);
                    break;
                case "email":
                    members = adminService.searchMembersByEmail(searchKeyword);
                    break;
                case "name":
                    members = adminService.searchMembersByName(searchKeyword);
                    break;
                default:
                    members = adminService.searchMembersAll(searchKeyword);
                    break;
            }
        } else {
            members = adminService.getAllMembers();
        }

        model.addAttribute("members", members);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);

        return "admin/admin-member";
    }
// 로그아웃
    @GetMapping("/admin/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // 세션 무효화
        }
        return "redirect:/";  // 로그인 페이지로 리다이렉트
    }
}
