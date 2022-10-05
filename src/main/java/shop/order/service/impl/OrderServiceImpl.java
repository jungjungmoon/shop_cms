package shop.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.manager.model.MemberParam;
import shop.member.repository.MemberRepository;
import shop.member.service.MemberService;
import shop.order.dto.OrderDto;
import shop.order.mapper.OrderMapper;
import shop.order.model.OrderParam;
import shop.order.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

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
}
