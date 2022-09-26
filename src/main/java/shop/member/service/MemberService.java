package shop.member.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import shop.member.model.MemberInput;
import shop.member.model.ResetPasswordInput;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput parameter);

    /**
     * uuid 해당하는 계정을 활성화 한다.
     */
    boolean emailAuth(String uuid);
}
