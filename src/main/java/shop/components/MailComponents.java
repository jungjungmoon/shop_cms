package shop.components;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailComponents {

    // Mail 전송 , 회원 진행
    private final JavaMailSender javaMailSender;

    public void sendMailTest() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("wjdans3476@naver.com");
        msg.setSubject("안녕하세요. SHOP 회원가입을 진행하세요");
        msg.setText("SHOP 입니다. 즐거운 쇼핑 되시길 바랍니다.");

        javaMailSender.send(msg);


    }

    public boolean sendMail(String mail, String subject, String text) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

}
