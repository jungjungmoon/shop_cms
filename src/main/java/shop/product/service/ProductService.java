package shop.product.service;

import shop.product.model.ProductInput;

public interface ProductService {

    /**
     * 상품등록
     */
    boolean add(ProductInput parameter);

}
