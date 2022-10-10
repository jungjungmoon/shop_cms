package shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import shop.manager.service.CategoryService;
import shop.product.dto.ProductDto;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
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
     * 파일 날짜 지정
     */
    String getNewSaveFile(String basePath, String origin){

        LocalDate now = LocalDate.now();

        String[] dirs = {
                String.format("%s/%d/", basePath,now.getYear()),
                String.format("%s/%d/%02d/", basePath,now.getYear(),now.getMonthValue()),
                String.format("%s/%d/%02d/%02d/", basePath,now.getYear(),now.getMonthValue(),now.getDayOfMonth())};

        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }

        String fileExtension = "";
        if (origin != null) {
            int i = origin.lastIndexOf(".");
            if (i > -1) {
                fileExtension = origin.substring(i + 1);
            }
        }

        // uuid 로 파일 확장자 진행
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 파일 확장자
        String newFilename = String.format("%s%s", dirs[2], uuid);
        if (fileExtension.length() > 0) {
            newFilename += "." + fileExtension;
        }

        return newFilename;
    }

    /**
     * 상품등록 페이지
     */
    @PostMapping(value = {"/manager/product/add.do", "/manager/product/edit.do"})
    public String addSubmit
    (
            Model model,
            HttpServletRequest request,
            MultipartFile files,
            ProductInput parameter

    ) {

        String saveFilename = "";

        /**
         * 파일 업로드 저장
         */
        if (files != null) {

            String origin = files.getOriginalFilename();
            String basePath = "C:\\shop_cms\\files";
            saveFilename = getNewSaveFile(basePath, origin);

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(files.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
                log.info("=============================== - 1");
                log.info(e.getMessage());
            }
        }

        parameter.setFilename(saveFilename);

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
