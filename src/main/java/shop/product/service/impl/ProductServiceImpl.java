package shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.manager.dto.MemberDto;
import shop.product.dto.ProductDto;
import shop.product.entity.Product;
import shop.product.mapper.ProductMapper;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.repository.ProductRepository;
import shop.product.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public boolean add(ProductInput parameter) {

        Product product = Product.builder()
                .subject(parameter.getSubject())
                .regDt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return true;
    }

    @Override
    public List<ProductDto> list(ProductParam parameter) {

        long totalCount = productMapper.selectListCount(parameter);

        // 페이지 처리
        // ProductDto 쪽에 totalCount 가지고 있다.
        List<ProductDto> list = productMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (ProductDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;

    }
}
