package shop.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.components.MailComponents;

/**
 *  가장 첫 page 시작
 *  MainPage 클래스를 만든 목적
 *  매핑하기 위해, 논리적인 인터넷주소, 물리적인파일 매핑
 */

@Controller
@RequiredArgsConstructor
public class MainController {

    // Mail 보내고 받는 Test
    private final MailComponents mailComponents;

    @RequestMapping("/")
    public String index() {

        /*
        String email = "wjdans3476@naver.com";
        String subject = "SHOP 입니다.";
        String text = "SHOP 에서 즐거운 쇼핑 되시길 바랍니다.";

        mailComponents.sendMail(email, subject, text);
        */
        return "index";
    }

}
