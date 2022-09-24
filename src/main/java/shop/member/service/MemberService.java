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

    /**
     * 비밀번호 초기화 정보 전송
     */
    boolean sendResetPassword(ResetPasswordInput parameter);

    /**
     * 입력받은 uuid에 대해 password 변경 진행
     */
    boolean resetPassword(String uuid, String password);

    /**
     * 입력 받은 uuid 값이 유효한지 확인
     */
    boolean checkResetPassword(String uuid);
}
