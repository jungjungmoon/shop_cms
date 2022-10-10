package shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Manager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.components.MailComponents;
import shop.manager.dto.MemberDto;
import shop.manager.mapper.MemberMapper;
import shop.manager.model.MemberParam;
import shop.member.entity.Member;
import shop.member.entity.MemberCode;
import shop.member.exception.MemberNotEmailAuthException;
import shop.member.exception.MemberSuspensionNotEmailAuthException;
import shop.member.model.MemberInput;
import shop.member.model.ResetPasswordInput;
import shop.member.repository.MemberRepository;
import shop.member.service.MemberService;
import shop.product.service.impl.ServiceResult;
import shop.util.PasswordUtils;

import javax.swing.plaf.basic.BasicViewportUI;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor // 생성자 주입
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    private final MemberMapper memberMapper;

    /**
     * 회원상태 변경 controller -> manager/customer/status
     */
    @Override
    public boolean updateStatus(String userId, String userStatus) {

        // username => email 값
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException(" 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }

        // 회원 정보 가져오는 부분 email
        Member member = optionalMember.get();

        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

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
                // 회원가입 할때 요청 상태
                .userStatus(Member.MEMBER_STATUS_REQ)
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
        // 계정 활성화가 이미 되었으면 차단
        if (member.isEmailAuthYn()){
            return false;
        }

        member.setUserStatus(Member.MEMBER_STATUS_USE);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    /**
     * 비밀번호 초기화 전송 저장
     */
    @Override
    public boolean sendResetPassword(ResetPasswordInput parameter) {

        // 비밀번호 찾기 조회 부분
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserNameAndPhone
                (
                        parameter.getUserId(),
                        parameter.getUserName(),
                        parameter.getPhone()
                );
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException(" 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }

        // 비밀번호 초기화 할때, 회원 일치확인, 비밀번호 날짜 지정
        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();
        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(3));
        memberRepository.save(member);

        // 사용자한테 링크를 보낸 후, 사이트에서 그 사람의 정보를 입력한 후 초기화 진행 방식
        String email = parameter.getUserId();
        String subject = "SHOP 사이트 입니다. 비밀번호 변경 부탁 드립니다. ";
        String text = "<P>SHOP 사이트 입니다. 비밀번호 변경 부탁 드립니다.</P><P>아래 링크를 클릭 후 비밀번호 변경 할 수 있습니다.</P>"
                + "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id=" + uuid + "'> 비밀번호 변경 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 비밀번호 재변경 설정 이메일(uuid)에 대해, (password) 재설정
     */
    @Override
    public boolean resetPassword(String uuid, String password) {

        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException(" 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }
        Member member = optionalMember.get();
        // 비밀번호 변경 하는 날짜 체크
       if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("비밀번호 변경 날짜가 아닙니다.");
        }

       if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
           throw new RuntimeException("비밀번호 변경 날짜가 아닙니다.");
       }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    /**
     * 날짜가 유효한지 확인하는 거 uuid
     * */
    @Override
    public boolean checkResetPassword(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }
        Member member = optionalMember.get();
        // 비밀번호 변경 하는 날짜 체크
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException(" 비밀번호 변경 날짜가 아닙니다.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException(" 비밀번호 변경 날짜가 아닙니다. ");
        }
        return true;
    }

    /**
     * mybatis 에서 데이터 값을 가져오는 부분 생성 - manager 부분에서 dto, memberMapper 추가 작업
     * 페이지 갯수 처리 부분 까지
     */
    @Override
    public List<MemberDto> list(MemberParam parameter) {

        long totalCount = memberMapper.selectListCount(parameter);

        // 페이지 처리
        // MemberDto 쪽에 totalCount 가지고 있다.
        List<MemberDto> list = memberMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    /**
     * 관리자 - 회원상세정보 페이지
     */
    @Override
    public MemberDto detail(String userId) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return null;
        }
        
        Member member = optionalMember.get();
        return MemberDto.of(member);
    }

    /**
     * 회원 비밀번호 변경 - 관리자에서
     */
    @Override
    public boolean updatePassword(String userId, String password) {

        // username => email 값
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException(" 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }

        // 회원 정보 가져오는 부분 email
        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setPassword(encPassword);
        memberRepository.save(member);

        return true;
    }

    /**
     * 일반회원 비밀번호 변경 - 회원정보
     */
    @Override
    public ServiceResult newPassword(MemberInput parameter) {

        String userId = parameter.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, " 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(parameter.getPassword(), member.getPassword())) {
            return new ServiceResult(false, " 회원 비밀번호가 일치하지 않습니다. 다시 확인해 주세요 ");
        }

        String encPassword = PasswordUtils.encPassword(parameter.getNewPassword());
        member.setPassword(encPassword);
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult newMember(MemberInput parameter) {

        String userId = parameter.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, " 회원 정보가 존재하지 않습니다. " + " 다시 확인해 주십시오. ");
        }
        Member member = optionalMember.get();

        member.setPhone(parameter.getPhone());
        member.setAddress(parameter.getAddress());
        member.setDetailAddress(parameter.getDetailAddress());
        member.setPostcode(parameter.getPostcode());
        memberRepository.save(member);

        return new ServiceResult(true);

    }

    /**
     * 일반회원 탈퇴
     */
    @Override
    public ServiceResult secession(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(password, member.getPassword())) {
           return new ServiceResult(false, "회원 비밀번호가 일치하지 않습니다. 확인후 변경해주세요.");
        }

        member.setUserName("탈퇴회원");
        member.setPhone("");
        member.setPassword("");
        member.setRegDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthKey("");
        member.setEmailAuthDt(null);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_SECESSION);
        member.setAddress("");
        member.setDetailAddress("");
        member.setPostcode("");
        member.setExtraAddress("");
        memberRepository.save(member);

        return new ServiceResult(true);
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
        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후 로그인을 하시길 바랍니다.");
        }

        // 회원 정지상태 일때
        if (Member.MEMBER_STATUS_SUSPENSION.equals(member.getUserStatus())){
            throw new MemberSuspensionNotEmailAuthException("정지된 회원 입니다.");
        }

        if (Member.MEMBER_STATUS_SECESSION.equals(member.getUserStatus())){
            throw new MemberSuspensionNotEmailAuthException("탈퇴한 회원 입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        //관리자 ROLE
        if (member.isManagerYn()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }

        // 스프링 시큐리티 에서 원하는 ROLE 3가지 추가 작업
        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
