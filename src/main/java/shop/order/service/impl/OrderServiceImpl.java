package shop.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.order.mapper.OrderMapper;
import shop.order.model.OrderParam;
import shop.order.service.OrderService;
import shop.order.dto.OrderDto;
import shop.order.entity.OrderStatus;
import shop.order.entity.ProductOrder;
import shop.order.repository.OrderRepository;
import shop.product.service.impl.ServiceResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    /**
     * 관리자 - 회원 주문관리 목록
     */
    @Override
    public List<OrderDto> list(OrderParam parameter) {

        long totalCount = orderMapper.selectListCount(parameter);

        // 페이지 처리
        // OrderDto 쪽에 totalCount 가지고 있다.
        List<OrderDto> list = orderMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (OrderDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    /**
     * 관리자 - 주문 상태 변경
     */
    @Override
    public ServiceResult updateStatus(long id, String status) {

        Optional<ProductOrder> optionalProductOrder = orderRepository.findById(id);
        if (!optionalProductOrder.isPresent()) {
            return new ServiceResult(false, "상품주문 신청이 없습니다.");
        }

        ProductOrder productOrder = optionalProductOrder.get();

        productOrder.setStatus(status);
        orderRepository.save(productOrder);

        return new ServiceResult(true);
    }

    /**
     * 내 장바구니
     */
    @Override
    public List<OrderDto> myBasket(String userId) {

        OrderParam orderParam = new OrderParam();
        orderParam.setUserId(userId);
        List<OrderDto> list = orderMapper.selectMyBasket(orderParam);

       return list;
    }

    /**
     * 내 장바구니 상품 취소
     */
    @Override
    public OrderDto detail(long id) {

        Optional<ProductOrder> productOrders = orderRepository.findById(id);
        if (productOrders.isPresent()){
            return OrderDto.of(productOrders.get());
        }
        return null;
    }

    @Override
    public ServiceResult del(long id) {

        Optional<ProductOrder> productOrders = orderRepository.findById(id);
        if (productOrders.isPresent()){
            return new ServiceResult(false, "상품이 없습니다.");
        }

        ProductOrder productOrder = productOrders.get();

        productOrder.setStatus(OrderStatus.STATUS_CANCEL);
        orderRepository.save(productOrder);

        return new ServiceResult(true);
    }

}
