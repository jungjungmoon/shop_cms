package shop.order.service;

import shop.order.dto.OrderDto;
import shop.order.model.OrderParam;

import java.util.List;

public interface OrderService {
    /**
     * 주문관리 목록
     */
    List<OrderDto> list(OrderParam parameter);

}
