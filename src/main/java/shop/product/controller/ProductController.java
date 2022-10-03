package shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.manager.service.CategoryService;
import shop.product.dto.ProductDto;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController extends BaseController {
    private final ProductService productService;
    private final CategoryService categoryService;

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
     * edit.do, add.do 화면인지 ? 어떠한 화면인지 확인
     */
    @GetMapping(value = {"/manager/product/add.do", "/manager/product/edit.do"})
    public String add(Model model, HttpServletRequest request, ProductInput parameter) {

        // 상품등록 화면이 보일때, 카테고리 정보도 함께 보여줘야 한다.
        model.addAttribute("category", categoryService.list());

        boolean edit = request.getRequestURI().contains("/edit.do");
        // 내용이 있다는 가정하에
        ProductDto detail = new ProductDto();

        if (edit) {
            long id = parameter.getId();
            ProductDto productDto = productService.getById(id);
            if (productDto == null) {
                //error
                model.addAttribute("message", "등록한 상품이 없습니다.");
                return "common/error";
            }
            detail = productDto;
        }
        model.addAttribute("edit", edit);
        model.addAttribute("detail", detail);

        return "manager/product/add";
    }

    /**
     * 상품등록 페이지
     */
    @PostMapping(value = {"/manager/product/add.do", "/manager/product/edit.do"})
    public String addSubmit(Model model, ProductInput parameter, HttpServletRequest request) {

        // 상품 수정
        boolean edit = request.getRequestURI().contains("/edit.do");

        if (edit) {
            long id = parameter.getId();
            ProductDto productDto = productService.getById(id);
            if (productDto == null) {
                //error
                model.addAttribute("message", "등록한 상품이 없습니다.");
                return "common/error";
            }
            boolean set = productService.set(parameter);

        } else {
            boolean add = productService.add(parameter);
        }

        return "redirect:/manager/product/list.do";
    }

    /**
     * 상품등록 삭제
     */
    @PostMapping("/manager/product/delete.do")
    public String delete(Model model, ProductInput parameter, HttpServletRequest request) {

        boolean delete = productService.delete(parameter.getIdList());


        return "redirect:/manager/product/list.do";
    }
}
