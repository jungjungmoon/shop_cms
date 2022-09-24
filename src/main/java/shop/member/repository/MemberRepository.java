package shop.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    // emailAuthKey 인터페이스에 저장 (사용자 찾기)
    Optional<Member> findByEmailAuthKey(String emailAuthKey);

    // 비밀번호 초기화 할때, 필요한 값 3가지
    Optional<Member> findByUserIdAndUserNameAndPhone(String userId, String userName, String phone);

    // 비밀번호 재변경 할때, uuid 값으로 받아와서 비밀번호 재 변경
    Optional<Member> findByResetPasswordKey(String resetPasswordKey);
}
