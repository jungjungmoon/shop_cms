package shop.member.entity;

/**
 *  회원상태 입력
 */


public interface MemberCode {

    // 회원 이용중인 상태
    String MEMBER_STATUS_USE = "USE";

    // 회원 정지상태
    String MEMBER_STATUS_SUSPENSION = "SUSPENSION";

    // 회원 현재가입 요청상태
    String MEMBER_STATUS_REQ = "REQ";

    // 회원 탈퇴
    String MEMBER_STATUS_SECESSION = "SECESSION";

}
