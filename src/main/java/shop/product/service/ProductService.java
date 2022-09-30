package shop.product.service;

import shop.product.dto.ProductDto;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;

import java.util.List;

public interface ProductService {

    /**
     * 상품등록
     */
    boolean add(ProductInput parameter);

    /**
     * 상품 목록
     */
    List<ProductDto> list(ProductParam parameter);

    /**
     * 상품 상세정보
     */
    ProductDto getById(long id);

    /**
     * 상품 상세정보 수정
     */
    boolean set(ProductInput parameter);

}
