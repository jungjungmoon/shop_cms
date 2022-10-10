package shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.order.model.OrderParam;
import shop.order.service.OrderService;
import shop.order.dto.OrderDto;
import shop.product.controller.BaseController;
import shop.product.dto.ProductDto;
import shop.product.entity.Product;
import shop.product.service.ProductService;
import shop.product.service.impl.ServiceResult;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ProductService productService;

    /**
     * 주문관리 목록 페이지
     */
    @GetMapping("/manager/order/list.do")
    public String list
    (
            Model model,
            OrderParam parameter,
            BindingResult bindingResult
    ) {

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

        List<ProductDto> productDtoLists = productService.listAll();
        model.addAttribute("productDtoLists", productDtoLists);

        return "manager/order/list";
    }

    /**
     * 주문 완료 페이지
     * order -> manager/order/status.do/
     */

    @PostMapping("/manager/order/status.do")
    public String status(Model model, OrderParam parameter) {

        ServiceResult status = orderService.updateStatus(parameter.getId(), parameter.getStatus());

        if (!status.isResult()) {
            model.addAttribute("message", status.getMessage());
            return "common/error";
        }

        return "redirect:/manager/order/list.do";
    }


}
