package shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String description;
    private String imagePath;
    private String keyword;
    private String subject;
    private String summary;
    private String contents;
    long price;
    long salePrice;
    LocalDateTime saleEndDt;

    // 등록일, 수정일 추가 작업
    LocalDateTime regDt;
    LocalDateTime udtDt;


    long totalCount;
    // 페이지 No 번호 정렬
    long seq;
}
