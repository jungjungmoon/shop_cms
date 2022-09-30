package shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import shop.util.PageUtil;

@Controller
@RequiredArgsConstructor
public class BaseController {

    /**
     * 페이징처리 화면에 보이는 부분, ProductController, managerCustomerController 상속
     */
    public String getPaperHtml(long totalCount, long pageSize, long pageIndex, String queryString) {

        PageUtil pageUtil = new PageUtil(totalCount, pageSize, pageIndex, queryString);
        return pageUtil.pager();
    }
}



