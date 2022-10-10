package shop.product.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * 제품
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    Long id;
    // 카테고리 선택
    long categoryId;

    String imagePath;
    String keyword;
    String subject;

    @Column(length = 1000)
    String summary;

    @Lob
    // 큰 데이터를 저장하는데 사용하는 데이터형
    String contents;
    long price;
    long salePrice;
    LocalDate saleEndDt;

    // 등록일, 수정일 추가 작업
    LocalDateTime regDt;
    LocalDateTime udtDt;

}
