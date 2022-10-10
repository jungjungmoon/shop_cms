package shop.order.model;

import lombok.Getter;
import lombok.Setter;
import shop.manager.model.CommonParam;

@Getter
@Setter
public class OrderParam extends CommonParam {
    /**
     * CommonParam 에 상속
     * 페이징 처리
     * 주문 관리목록
     */

    long id;
    String status;
    String userId;
    long productId;

}
