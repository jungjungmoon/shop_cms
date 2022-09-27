package shop.manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.manager.dto.CategoryDto;
import shop.manager.dto.MemberDto;
import shop.manager.model.CategoryInput;
import shop.manager.model.MemberParam;
import shop.manager.model.MemberStatusInput;
import shop.manager.service.CategoryService;
import shop.member.service.MemberService;
import shop.util.PageUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class managerCategoryController {

    private final CategoryService categoryService;

    /**
     * 관리자 상품 카테고리 페이지
     */
    @GetMapping("/manager/category/list.do")
    public String list(Model model, MemberParam parameter) {

        List<CategoryDto> list = categoryService.list();
        model.addAttribute("list", list);

        return "manager/category/list";
    }

    /**
     * 카테고리 등록
     */
    @PostMapping("/manager/category/add.do")
    public String add(Model model, CategoryInput parameter){

        boolean add = categoryService.add(parameter.getCategoryName());

        return "redirect:/manager/category/list.do";
    }

}
