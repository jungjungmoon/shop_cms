package shop.manager.dto;

import lombok.*;
import shop.member.entity.Member;

import java.time.LocalDateTime;

/**
 *  데이터 베이스에서 내리는 테이블 (Member 값들)
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    String userStatus;

    long totalCount;
    // 페이지 No 번호 정렬
    long seq;


    // of 메서드로 구현 해서 ServiceImpl 가져올 수 있음
    public static MemberDto of(Member member) {

        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .phone(member.getPhone())
                .address(member.getAddress())
                .detailAddress(member.getDetailAddress())
                .extraAddress(member.getExtraAddress())
                .postcode(member.getPostcode())
                .emailAuthYn(member.isEmailAuthYn())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthKey(member.getEmailAuthKey())
                .regDt(member.getRegDt())
                .resetPasswordKey(member.getResetPasswordKey())
                .resetPasswordLimitDt(member.getResetPasswordLimitDt())
                .managerYn(member.isManagerYn())
                .userStatus(member.getUserStatus())
                .build();
    }
}
