package shop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import shop.member.service.MemberService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    // 로그인 시큐리티 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 사용자 의지와 무관하게 공격자의 의도대로 서버에 특정 요청을 하도록 함, csrf 추가, 보안적 이슈가 있음
        http.csrf().disable();
        http.cors().disable();
        http.headers().frameOptions().sameOrigin();

        // 로그인 권한을 모두 허용 하겠다 -> / , /** 이 모든 페이지에 대해 접근 가능
        // 로그인을 안해도, 3개의 페이지는 들어갈 수 있도록, 지정 할 수 있음
        http.authorizeRequests()
                .antMatchers("/",
                        "/member/register",
                        "/member/email-auth",
                        "/member/find/password",
                        "/member/reset/password"
                )
                .permitAll();
        // 관리자가 아닌경우 접근 불가
        http.authorizeRequests()
                .antMatchers("/manager/**")
                .hasAuthority("ROLE_MANAGER");

        // 로그인 페이지 지정
        http.formLogin()
                .loginPage("/member/login")
                .failureHandler(getFailureHandler())
                .permitAll();

        // 로그아웃 페이지 지정
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                .accessDeniedPage("/error/denied");

        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());

        super.configure(auth);
    }
}
