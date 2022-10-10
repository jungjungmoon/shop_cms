package shop.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    // emailAuthKey 인터페이스에 저장 (사용자 찾기)
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
}
