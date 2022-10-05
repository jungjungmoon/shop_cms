package shop.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import shop.order.dto.OrderDto;
import shop.order.model.OrderParam;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 페이징처리 mybatis
     */
    List<OrderDto> selectList(OrderParam parameter);

    /**
     * 페이징 갯수 카운트
     */
    long selectListCount(OrderParam parameter);
}
