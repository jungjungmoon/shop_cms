package shop.product.model;

import lombok.Getter;
import lombok.Setter;
import shop.manager.model.CommonParam;

@Getter
@Setter
public class ProductParam extends CommonParam {
    // 페이징 처리
    /**
     * CommonParam 에 상속
     */

    long categoryId;
    long id;
}
