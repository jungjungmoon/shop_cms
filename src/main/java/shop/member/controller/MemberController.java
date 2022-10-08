package shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.manager.dto.MemberDto;
import shop.member.model.MemberInput;
import shop.member.model.ResetPasswordInput;
import shop.member.service.MemberService;
import shop.product.service.impl.ServiceResult;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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
     * 회원 정보 수정 구현
     */
    @GetMapping("/member/info")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/info";
    }

    /**
     * 회원 비밀번호 변경
     */
    @GetMapping("/member/password")
    public String memberPassword(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/password";
    }
    @PostMapping("/member/password")
    public String memberPasswordSubmit(Model model, Principal principal, MemberInput parameter) {

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.newPassword(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }

    /**
     * 회원 장바구니
     */
    @GetMapping("/member/userproduct")
    public String memberUserProduct(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/userproduct";
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

    /**
     * 비밀번호 찾기
     */
    @GetMapping("/member/find/password")
    public String findPassword() {

        return "member/find_password";
    }

    /**
     * 비밀번호 찾기 결과
     */
    @PostMapping("/member/find/password")
    public String findPasswordSubmit(
            Model model,
            ResetPasswordInput parameter
    ) {
        boolean result = false;
        try {
            result = memberService.sendResetPassword(parameter);
        } catch (Exception e) {
        }
        model.addAttribute("result", result);

        return "member/find_password_result";
    }

    /**
     * 사용자가 이메일 받은 후, 비밀번호 변경 하는 부분
     */
    @GetMapping("/member/reset/password")
    public String resetPassword(
            Model model,
            HttpServletRequest request
    ) {
        String uuid = request.getParameter("id");

        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(
            Model model,
            ResetPasswordInput parameter
    ) {

        boolean result = false;
        try {
             result = memberService.resetPassword(parameter.getId(), parameter.getPassword());
        }catch (Exception e) {
        }

        model.addAttribute("result", result);

        return "member/reset_password_result";
    }

}
