package shop.member.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 *  회원가입 할때 필요한,  DB테이블 저장
 */
@Entity
@Getter
@Setter
public class Member {

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


}
