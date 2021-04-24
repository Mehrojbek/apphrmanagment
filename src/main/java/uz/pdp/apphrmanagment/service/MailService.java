package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;

    public boolean sendEmail(String sendingEmail, String message, boolean verifyEmail, String subject){

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            String text;
            if (verifyEmail) {
                 text= "http://localhost:8080/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + message;
            }else {
                text=message;
            }

            mailMessage.setFrom("Company");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
