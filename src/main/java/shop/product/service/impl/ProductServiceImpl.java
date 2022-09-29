package shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.product.entity.Product;
import shop.product.model.ProductInput;
import shop.product.repository.ProductRepository;
import shop.product.service.ProductService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public boolean add(ProductInput parameter) {

        Product product = Product.builder()
                .subject(parameter.getSubject())
                .regDt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return true;
    }
}
