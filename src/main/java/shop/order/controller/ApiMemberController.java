package shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.configuration.ResponseResult;
import shop.member.service.MemberService;
import shop.order.dto.OrderDto;
import shop.order.model.OrderInput;
import shop.order.service.OrderService;
import shop.product.service.impl.ServiceResult;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiMemberController {

    private final MemberService memberService;
    private final OrderService orderService;


    @PostMapping("/api/member/product/cancel.api")
    public ResponseEntity<?> productDel
            (
                    Model model,
                    Principal principal,
                    @RequestBody OrderInput parameter
            ) {

        String userId = principal.getName();

        /**
         *  내 상품 목록만 취소
         *  내 log한 값 -> principal 주입
         */
        OrderDto orderDto = orderService.detail(parameter.getProductOrderId());
        if (orderDto == null) {
            ResponseResult responseResult = new ResponseResult(false, "상품이 없습니다.");
            return ResponseEntity.ok().body(responseResult);
        }
        if (userId == null || !userId.equals(orderDto.getUserId())) {
            ResponseResult responseResult = new ResponseResult(false, "내 상품만 처리가능합니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        ServiceResult result = orderService.del(parameter.getProductOrderId());
        if (!result.isResult()){
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);

    }
}
