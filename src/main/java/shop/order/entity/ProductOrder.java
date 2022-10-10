package shop.order.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder implements OrderStatus {

    /**
     * 상품주문 신청
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    Long id;
    long productId;
    String userId;
    // 금액
    long payPrice;
    // 상품신청 신청, 취소, 완료
    String status;
    LocalDateTime regDt;


}
