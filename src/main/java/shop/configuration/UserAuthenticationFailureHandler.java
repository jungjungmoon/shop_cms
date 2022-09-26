package shop.configuration;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // 로그인 페이지 처리 에러메시지
    @Override
    public void onAuthenticationFailure
            (
                    HttpServletRequest request,
                    HttpServletResponse response,
                    AuthenticationException exception) throws IOException,
                    ServletException {

        String msg = " 로그인에 실패 하였습니다. " + " 다시 확인해 주십시오. ";

        // 로그인 에러 메시지
        if (exception instanceof InternalAuthenticationServiceException) {
            msg = exception.getMessage();

        }

        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        request.setAttribute("errorMessage",
                " 로그인에 실패 하였습니다. " + " 다시 확인해 주십시오. ");

//        System.out.println("로그인에 실패하였습니다.");

        super.onAuthenticationFailure(request, response, exception);
    }
}
