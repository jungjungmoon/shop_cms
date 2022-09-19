package shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.member.entity.Member;
import shop.member.model.MemberInput;
import shop.member.repository.MemberRepository;
import shop.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor // 자동생성자 주입 -> MemberService
public class MemberController {

    /**
     * 회원 가입 부분
     */
    private final MemberService memberService;


    @GetMapping("/member/register")
    public String register() {

        return "member/register";
    }
    // PostMapping url 화면에서 버튼을 눌렀을 때, 바뀌는 부분
    @PostMapping("/member/register")
    public String registerSubmit(
            Model model,
            HttpServletRequest request,
            MemberInput parameter) {

        boolean result = memberService.register(parameter);

        model.addAttribute("result", result);

        return "member/register_complete";
    }

}
