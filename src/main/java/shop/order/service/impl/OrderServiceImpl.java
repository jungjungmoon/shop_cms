package shop.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.order.dto.OrderDto;
import shop.order.entity.ProductOrder;
import shop.order.mapper.OrderMapper;
import shop.order.model.OrderParam;
import shop.order.repository.OrderRepository;
import shop.order.service.OrderService;
import shop.product.service.impl.ServiceResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

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

}
