package uz.pdp.apphrmanagment.component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.RoleName;

import java.util.*;
import java.util.Collection;

@Component
public class AllMethod {

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


}
