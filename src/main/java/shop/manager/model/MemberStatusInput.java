package shop.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberStatusInput {

    /**
     * 회원 상태 PostMapping 처리 부분
     */

    String userId;
    String userStatus;
    String password;


}


