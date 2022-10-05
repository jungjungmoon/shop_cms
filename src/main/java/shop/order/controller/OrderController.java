package shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import shop.manager.model.MemberParam;
import shop.order.dto.OrderDto;
import shop.order.model.OrderParam;
import shop.order.service.OrderService;
import shop.product.controller.BaseController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController extends BaseController {

    private final OrderService orderService;

    /**
     * 주문관리 목록 페이지
     */
    @GetMapping("/manager/order/list.do")
    public String list(Model model, OrderParam parameter) {

        parameter.init();

        List<OrderDto> orderDtos = orderService.list(parameter);

        // 페이지 처리 갯수 단계
        long totalCount = 0;
        if (!CollectionUtils.isEmpty(orderDtos)) {
            totalCount = orderDtos.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();

        // 페이징 처리 ( 화면에 보이는 부분 )
        String pagerHtml = getPaperHtml
                (
                        totalCount, parameter.getPageSize(),
                        parameter.getPageIndex(),
                        parameter.getQueryString()
                );

        model.addAttribute("list", orderDtos);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "manager/order/list";
    }


}
