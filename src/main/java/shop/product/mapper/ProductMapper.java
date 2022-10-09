package shop.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import shop.order.dto.OrderDto;
import shop.order.model.OrderParam;
import shop.product.dto.ProductDto;
import shop.product.model.ProductParam;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * 페이징처리 mybatis
     */
    List<ProductDto> selectList(ProductParam parameter);

    // 페이징 갯수 카운트
    long selectListCount(ProductParam parameter);

}
