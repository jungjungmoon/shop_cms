package shop.product.dto;

import lombok.*;
import shop.product.entity.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private Long categoryId;
    private String imagePath;
    private String keyword;
    private String subject;
    private String summary;
    private String contents;
    long price;
    long salePrice;
    LocalDate saleEndDt;

    // 등록일, 수정일 추가 작업
    LocalDateTime regDt;
    LocalDateTime udtDt;


    long totalCount;
    // 페이지 No 번호 정렬
    long seq;

    public static ProductDto of(Product product) {

        return ProductDto.builder()
                .id(product.getId())
                .imagePath(product.getImagePath())
                .keyword(product.getKeyword())
                .subject(product.getSubject())
                .summary(product.getSummary())
                .contents(product.getContents())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .regDt(product.getRegDt())
                .udtDt(product.getUdtDt())
                .categoryId(product.getCategoryId())
                .build();

    }
}
