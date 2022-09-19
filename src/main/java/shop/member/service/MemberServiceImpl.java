package shop.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.member.entity.Member;
import shop.member.model.MemberInput;
import shop.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 생성자 주입
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입 중복확인
     */
    @Override
    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()) {
            // 현재 userId에 해당하는 데이터 존재
            return false;
        }

        Member member = new Member();
        member.setUserId(parameter.getUserId());
        member.setPassword(parameter.getPassword());
        member.setRegDt(LocalDateTime.now());
        member.setUserName(parameter.getUserName());
        memberRepository.save(member);
        return false;
    }
}
