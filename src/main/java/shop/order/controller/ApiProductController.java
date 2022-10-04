package shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.manager.service.CategoryService;
import shop.order.model.OrderInput;
import shop.product.controller.BaseController;
import shop.product.service.ProductService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiProductController extends BaseController {
    private final ProductService productService;
    private final CategoryService categoryService;

    /**
     * 상품주문 신청 api, login 한 값을 찾아 자동 주입
     */
    @PostMapping("/api/product/req.api")
    public ResponseEntity<?> productRequest(
            Model model,
            // body -> RequestBody 추가
            @RequestBody OrderInput parameter,
            Principal principal
    ) {
        // 내 로그인한 Id -> Principal (login 자동 주입)
        parameter.setUserId(principal.getName());

        // 상품주문이 전달이 안됐을때
        boolean req = productService.req(parameter);
        if (!req){
            return ResponseEntity.badRequest().body("상품주문 취소 되었습니다.");
        }

        return ResponseEntity.ok().body(parameter);


    }

}
