package shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.member.model.MemberInput;
import shop.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor // 자동생성자 주입 -> MemberService
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입 부분
     */
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

    /**
     * 웹 주소 emailAuth 활성화 생성
     */
    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email_auth";
    }

    /**
     * 회원 정보 페이지 처리
     */
    @GetMapping("/member/info")
    public String memberInfo() {

        return "member/info";
    }

    /**
     * login 페이지 처리
     * GetMapping 으로 하면, post를 지원할 수 없다고 하여,
     * RequestMapping 진행.
     * < Request method 'POST' not supported > 오류 문구
     */
    @RequestMapping("/member/login")
    public String login() {

        return "member/login";
    }

}
