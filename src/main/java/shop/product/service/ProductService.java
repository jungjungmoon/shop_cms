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

    /**
     * 상품 삭제
     */
    boolean delete(String idList);

    /**
     * 일반회원 상품 목록 (관리자 != 일반회원)
     */
    List<ProductDto> frontList(ProductParam parameter);

}
