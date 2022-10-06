package shop.order.dto;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    /**
     * 주문 관리
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private long productId;
    private String userId;
    // 금액
    private long payPrice;
    // 상품신청 신청, 취소, 완료
    private String status;
    private LocalDateTime regDt;
    private String userName;
    private String phone;
    private String subject;
    long totalCount;
    // 페이지 No 번호 정렬
    long seq;

    public String getRegDtText() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return regDt != null ? regDt.format(dateTimeFormatter) : "";

    }


}
