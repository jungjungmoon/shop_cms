package shop.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberInput {
    // register.html --> 있는 정보들

    private String userId;
    private String userName;
    private String password;
    private String phone;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private String newPassword;
}
