package shop.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetPasswordInput {
    // find_password

    /**
     * 비밀번호 찾기
     */
    private String userId;
    private String userName;
    private String phone;

    /**
     * 비밀번호 재입력 할때 필요한 부분
     */
    private String password;
    private String id;

}
