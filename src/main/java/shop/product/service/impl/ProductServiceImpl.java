package shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.product.dto.ProductDto;
import shop.product.entity.Product;
import shop.product.mapper.ProductMapper;
import shop.product.model.ProductInput;
import shop.product.model.ProductParam;
import shop.product.repository.ProductRepository;
import shop.product.service.ProductService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // 할인 종료일
    private LocalDate getLocalDate(String value) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean add(ProductInput parameter) {

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDt());

        Product product = Product.builder()
                .subject(parameter.getSubject())
                .regDt(LocalDateTime.now())
                .categoryId(parameter.getCategoryId())
                .keyword(parameter.getKeyword())
                .summary(parameter.getSummary())
                .contents(parameter.getContents())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                .saleEndDt(saleEndDt)
                .build();

        productRepository.save(product);

        return true;
    }

    /**
     * 페이지 처리
     */
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

    /**
     * 상품 상세정보 id 찾기
     */
    @Override
    public ProductDto getById(long id) {

        return productRepository.findById(id).map(ProductDto::of).orElse(null);

    }

    /**
     * 상품 수정 부분
     */
    @Override
    public boolean set(ProductInput parameter) {

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDt());

        Optional<Product> optionalProducts = productRepository.findById(parameter.getId());
        if (!optionalProducts.isPresent()) {
            // 수정 x
            return false;
        }

        Product product = optionalProducts.get();

        product.setSubject(parameter.getSubject());
        product.setUdtDt(LocalDateTime.now());
        product.setCategoryId(parameter.getCategoryId());
        product.setKeyword(parameter.getKeyword());
        product.setSummary(parameter.getSummary());
        product.setContents(parameter.getContents());
        product.setPrice(parameter.getPrice());
        product.setSalePrice(parameter.getSalePrice());
        product.setSaleEndDt(saleEndDt);
        productRepository.save(product);

        return true;
    }

    @Override
    public boolean delete(String idList) {

        if (idList != null && idList.length() > 0) {

            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {
                }

                if (id > 0) {
                    productRepository.deleteById(id);
                }
            }
        }

        return true;
    }
}
