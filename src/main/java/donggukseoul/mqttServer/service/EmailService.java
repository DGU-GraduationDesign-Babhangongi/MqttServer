package donggukseoul.mqttServer.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("회원가입 인증 코드");

        String htmlContent = "<html>" +
                "<body>" +
                "<h1>회원가입 인증 코드</h1>" +
                "<p>인증 코드는 다음과 같습니다:</p>" +
                "<h2>" + code + "</h2>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendNotificationEmail(String to, String classroomName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("제어기기 상태 알림");

        String htmlContent = "<html>" +
                "<body>" +
                "<h1>제어기기 상태 변경 알림</h1>" +
                "<p>관할구역 " + classroomName + "의 제어기기가 켜졌습니다.</p>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}