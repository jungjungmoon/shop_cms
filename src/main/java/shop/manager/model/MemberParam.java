package shop.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberParam extends CommonParam{

    /**
     * CommonParam 에 상속
     */

    // 회원상세 페이지 볼때 파라미터값
    String userId;


}
