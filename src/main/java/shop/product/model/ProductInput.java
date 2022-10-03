package shop.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInput {

    long id;
    String subject;
    long categoryId;
    String keyword;
    String summary;
    String contents;
    long price;
    long salePrice;
    String saleEndDt;

    // 상품삭제
    String idList;


}
