package shop.order.service;

import shop.order.dto.OrderDto;
import shop.order.model.OrderParam;
import shop.product.service.impl.ServiceResult;

import java.util.List;

public interface OrderService {
    /**
     * 주문관리 목록
     */
    List<OrderDto> list(OrderParam parameter);

    /**
     * 주문 상태 변경
     */
    ServiceResult updateStatus(long id, String status);

}
