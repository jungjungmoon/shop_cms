package shop.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.manager.dto.MemberDto;
import shop.manager.model.MemberParam;
import shop.member.entity.Member;
import shop.member.service.MemberService;
import shop.util.PageUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class managerCustomerController {

    private final MemberService memberService;

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

        PageUtil pageUtil = new PageUtil(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pageUtil.pager());

        return "manager/customer/list";
    }
}
