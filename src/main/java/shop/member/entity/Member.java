package shop.member.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 *  회원가입 할때 필요한,  DB테이블 저장
 */
@Entity
@Getter
@Setter
@Builder // MemberServiceImpl 에서 builder 사용 할 수 있다.
@NoArgsConstructor
@AllArgsConstructor
public class Member implements MemberCode {

    @Id
    private String userId;
    private String userName;
    private String password;
    private String phone;
    private LocalDateTime regDt;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

    /**
     * 이메일 정상 처리 확인 부분
     */
    private boolean emailAuthYn; // 메일 인증 정상적으로 처리 확인
    private String emailAuthKey; // 회원가입 할때 key 생성, 이메일로 보내주고, 이메일 링크를 타고 와서 emailAuthYn true 바꿔줘서 확인.
    private LocalDateTime emailAuthDt; // 이메일 인증 날짜 확인


}
