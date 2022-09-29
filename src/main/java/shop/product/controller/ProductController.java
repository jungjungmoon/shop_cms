package shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.product.model.ProductInput;
import shop.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/manager/product/list.do")
    public String list(Model model){

        return "manager/product/list";
    }

    /**
     * 상품등록 화면
     */
    @GetMapping("/manager/product/add.do")
    public String add(Model model){

        return "manager/product/add";
    }

    /**
     * 상품등록 페이지
     */
    @PostMapping("/manager/product/add.do")
    public String addSubmit(Model model, ProductInput parameter){

        boolean add = productService.add(parameter);

        return "redirect:/manager/product/list.do";
    }
}
