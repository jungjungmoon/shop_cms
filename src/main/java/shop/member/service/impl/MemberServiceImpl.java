package shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import shop.components.MailComponents;
import shop.member.entity.Member;
import shop.member.exception.MemberNotEmailAuthException;
import shop.member.model.MemberInput;
import shop.member.repository.MemberRepository;
import shop.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // 생성자 주입
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

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

        /**
         * 회원가입 한 이후
         */

        String uuid = UUID.randomUUID().toString();
        // 로그인 할때, 비밀번호를 암호화 해주고 처리 진행
        // SecurityConfiguration ->  .passwordEncoder(getPasswordEncoder());
        // 값으로 가져왔기 때문에, 변경 사항
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(encPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .address(parameter.getAddress())
                .detailAddress(parameter.getDetailAddress())
                .extraAddress(parameter.getExtraAddress())
                .postcode(parameter.getPostcode())
                .build();
        memberRepository.save(member);

        /**
         * 이메일 전송
         */

        String email = parameter.getUserId();
        String subject = "SHOP 사이트 가입해주셔서 감사합니다. ";
        String text = "<P>즐거운 쇼핑 시간되세요.</P><P>아래 링크를 가입 완료 후 서비스 진행 할 수 있습니다.</P>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'> 가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 이메일 활성화 메서드
     */
    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    /**
     * 로그인 페이지 값들 지정, 시큐리티 지정을 해준다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // username => email 값
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException(" 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }

        // 회원 정보 가져오는 부분 email
        Member member = optionalMember.get();
        // 메일 인증 정상적으로 처리 확인
        if (!member.isEmailAuthYn()) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후 로그인을 하시길 바랍니다.");

        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 스프링 시큐리티 에서 원하는 ROLE 3가지 추가 작업
        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
