package uz.pdp.apphrmanagment.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagment.entity.Role;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.RoleName;
import uz.pdp.apphrmanagment.repository.RoleRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.*;
import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")){

            List<Role> roles = new ArrayList<>(
                    Arrays.asList(
                            new Role(RoleName.ROLE_WORKER),
                            new Role(RoleName.ROLE_MANAGER),
                            new Role(RoleName.ROLE_DIRECTOR)
                    )
            );
            roleRepository.saveAll(roles);


            User director = new User();
            director.setFirstName("user");
            director.setLastName("user");
            director.setEmail("user@gmail.com");
            director.setPassword(passwordEncoder.encode("1234"));
            director.setEmailCode(null);
            director.setEnabled(true);
            director.setRoles(Collections.singleton(roleRepository.getByName(RoleName.ROLE_DIRECTOR)));
            userRepository.save(director);

        }
    }
}
