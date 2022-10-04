package shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.manager.dto.CategoryDto;
import shop.manager.service.CategoryService;
import shop.product.dto.ProductDto;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberProductController extends BaseController {
    private final ProductService productService;
    private final CategoryService categoryService;

    /**
     * localhost8080 화면에 상품목록 페이지 구현 (비회원, 로그인 전 상품 목록 볼 수 있게)
     */
    @GetMapping("/product")
    public String product(Model model, ProductParam parameter) {

        List<ProductDto> productDtos = productService.frontList(parameter);
        model.addAttribute("productDtos", productDtos);

        List<CategoryDto> categoryDtos = categoryService.frontList(CategoryDto.builder().build());

        int productTotalCount = 0;
        if (categoryDtos != null){
            for (CategoryDto x : categoryDtos) {
                productTotalCount += x.getProductCount();
            }
        }
        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("productTotalCount", productTotalCount);

        return "product/index";
    }


}