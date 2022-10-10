package shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.order.entity.OrderStatus;
import shop.order.entity.ProductOrder;
import shop.order.model.OrderInput;
import shop.order.repository.OrderRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;

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
                .filename(parameter.getFilename())
                .build();

        productRepository.save(product);

        return true;
    }

    /**
     * 관리자만 -> 회원목록처리
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
        product.setFilename(parameter.getFilename());

        productRepository.save(product);

        return true;
    }

    /**
     * 상품 삭제 기능 구현
     */
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

    /**
     * 상품목록 구현, 일반 회원이 localhost8080 서버 일때, 보여지는 부분
     * 기존 페이징 처리 x
     */
    @Override
    public List<ProductDto> frontList(ProductParam parameter) {

        if (parameter.getCategoryId() < 1) {
            List<Product> products = productRepository.findAll();
            return ProductDto.of(products);
        }
        return productRepository.findByCategoryId(parameter.getCategoryId()).map(ProductDto::of).orElse(null);

    }

    /**
     * 상품상세정보 목록 (일반회원) -> 상품목록 클릭시 들어가는 페이지
     */
    @Override
    public ProductDto frontDetail(long id) {

        Optional<Product> optionalProducts = productRepository.findById(id);
        if (optionalProducts.isPresent()) {
            return ProductDto.of(optionalProducts.get());
        }

        return null;
    }

    /**
     * 상품주문 신청 api
     * userId, 상품정보
     */
    @Override
    public ServiceResult req(OrderInput parameter) {

        ServiceResult result = new ServiceResult();

        Optional<Product> optionalProduct = productRepository.findById(parameter.getProductId());
        if (!optionalProduct.isPresent()) {

            result.setResult(true);
            result.setMessage("상품 주문을 할 수 없습니다.");
            return result;
        }

        Product product = optionalProduct.get();

        String[] status = {OrderStatus.STATUS_REQ, OrderStatus.STATUS_COMPLETE};
        long count = orderRepository.countByProductIdAndUserIdAndStatusIn
                (
                        product.getId(),
                        parameter.getUserId(),
                        Arrays.asList(status)
                );

        // 상품주문이 실행, 중복 x
        if (count > 0) {
            result.setResult(true);
            result.setMessage("주문한 상품이 있습니다.");
            return result;
        }

        ProductOrder productOrder = ProductOrder.builder()
                .productId(product.getId())
                .userId(parameter.getUserId())
                .payPrice(product.getSalePrice())
                .status(ProductOrder.STATUS_REQ)
                .regDt(LocalDateTime.now())
                .build();
        orderRepository.save(productOrder);

        result.setResult(true);
        result.setMessage("");
        return result;
    }

    @Override
    public List<ProductDto> listAll() {

        List<Product> products = productRepository.findAll();
        return ProductDto.of(products);
    }

}
