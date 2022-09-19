package shop.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.member.entity.Member;
import shop.member.repository.MemberRepository;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        //given
        Member member = new Member();
        member.setUserId("kim");

        //when
        memberRepository.findById(member.getUserId());

        //then
        System.out.println(member.getUserId());

    }
}