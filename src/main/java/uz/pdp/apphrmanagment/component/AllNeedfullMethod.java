package uz.pdp.apphrmanagment.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.RoleName;

import java.util.*;
import java.util.Collection;

@Component
public class AllNeedfullMethod {
    @Autowired
    JavaMailSender javaMailSender;

    //GET ROLE NUMBER
    public byte getRoleNumber(Collection<? extends GrantedAuthority> authorities){
        byte roleNumber = 0;

        //GET ROLE FROM USER
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(RoleName.ROLE_MANAGER.name())) {
                roleNumber = 1;
            }

            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())) {
                roleNumber = 2;
            }
        }
        return roleNumber;
    }





    //BAJARUVCHI KIMLIGINI ANIQLASH
    public byte getPerformers(List<User> userList){

        for (User user : userList) {
            byte roleNumber = getRoleNumber(user.getAuthorities());
            if (roleNumber==0)
                return 0;
            if (roleNumber==1)
                return 1;
        }

        return -1;
    }





    //MAIL SENDER
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
