package shop.member.exception;

// 정지된 회원 일때, 에러
public class MemberSuspensionNotEmailAuthException extends RuntimeException {
    public MemberSuspensionNotEmailAuthException(String error) {
        super(error);
    }
}
