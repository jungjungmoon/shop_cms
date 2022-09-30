package shop.manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.manager.dto.MemberDto;
import shop.manager.model.MemberParam;
import shop.manager.model.MemberStatusInput;
import shop.member.service.MemberService;
import shop.product.controller.BaseController;
import shop.util.PageUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class managerCustomerController extends BaseController {

    private final MemberService memberService;

    /**
     * 관리자 회원관리 페이지
     */
    @GetMapping("/manager/customer/list.do")
    public String manager(Model model, MemberParam parameter) {

        parameter.init();

        List<MemberDto> members = memberService.list(parameter);

        // 페이지 처리 갯수 단계
        long totalCount = 0;
        if (members != null && members.size() > 0){
            totalCount = members.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();

        // 페이징 처리 ( 화면에 보이는 부분 )
        String pagerHtml = getPaperHtml
                (
                        totalCount, parameter.getPageSize(),
                        parameter.getPageIndex(),
                        parameter.getQueryString()
                );

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "manager/customer/list";
    }

    /**
     * 관리자 - 회원상세정보 페이지
     */
    @GetMapping("/manager/customer/detail.do")
    public String detail(Model model, MemberParam parameter) {

        parameter.init();

        MemberDto member = memberService.detail(parameter.getUserId());
        model.addAttribute("member", member);


        return "manager/customer/detail";
    }

    /**
     *  회원상태 변경
     */
    @PostMapping("/manager/customer/status.do")
    public String status(Model model, MemberStatusInput parameter) {

        boolean b = memberService.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        return "redirect:/manager/customer/detail.do?userId=" + parameter.getUserId();
    }

    /**
     * 회원 비밀번호 변경해서 관리 할 수 있음.
     */
    @PostMapping("/manager/customer/password.do")
    public String password(Model model, MemberStatusInput parameter) {

        boolean b = memberService.updatePassword(parameter.getUserId(), parameter.getPassword());

        return "redirect:/manager/customer/detail.do?userId=" + parameter.getUserId();
    }

}
