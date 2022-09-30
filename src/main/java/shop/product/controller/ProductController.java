package shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.product.dto.ProductDto;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController extends BaseController {
    private final ProductService productService;

    /**
     * 상품등록 목록 페이지
     */
    @GetMapping("/manager/product/list.do")
    public String list(Model model, ProductParam parameter) {

        parameter.init();

        List<ProductDto> productDtos = productService.list(parameter);

        // 페이지 처리 갯수 단계
        long totalCount = 0;
        if (!CollectionUtils.isEmpty(productDtos)) {
            totalCount = productDtos.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();

        // 페이징 처리 ( 화면에 보이는 부분 )
        String pagerHtml = getPaperHtml
                (
                        totalCount, parameter.getPageSize(),
                        parameter.getPageIndex(),
                        parameter.getQueryString()
                );

        model.addAttribute("list", productDtos);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "manager/product/list";
    }

    /**
     * 상품등록 화면
     */
    @GetMapping("/manager/product/add.do")
    public String add(Model model) {

        return "manager/product/add";
    }

    /**
     * 상품등록 페이지
     */
    @PostMapping("/manager/product/add.do")
    public String addSubmit(Model model, ProductInput parameter) {

        boolean add = productService.add(parameter);

        return "redirect:/manager/product/list.do";
    }
}
