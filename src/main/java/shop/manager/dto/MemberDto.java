package shop.manager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  데이터 베이스에서 내리는 테이블 (Member 값들)
 */

@Getter
@Setter
public class MemberDto {

    String userId;
    String userName;
    String password;
    String phone;
    LocalDateTime regDt;
    String address;
    String detailAddress;
    String extraAddress;
    String postcode;

    String emailAuthKey;
    boolean emailAuthYn;
    LocalDateTime emailAuthDt;

    String resetPasswordKey;
    LocalDateTime resetPasswordLimitDt;

    boolean managerYn;

    long totalCount;
    // 페이지 No 번호 정렬
    long seq;
}
