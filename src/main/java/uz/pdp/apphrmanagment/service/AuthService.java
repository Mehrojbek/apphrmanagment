package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.RoleName;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.LoginDto;
import uz.pdp.apphrmanagment.payload.RegisterDto;
import uz.pdp.apphrmanagment.repository.RoleRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;
import uz.pdp.apphrmanagment.security.JwtProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MailService mailService;
    @Autowired
    JwtProvider jwtProvider;


    //REGISTER NEW WORKER OR MANAGER
    public ApiResponse register(RegisterDto registerDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //DIRECTOR TIZIM ISHGA TUSHGANDA QO'SHILGANLIGI SABABLI HECH QANDAY ANONYMOUS_USER BO'LISHI MUMKIN EMAS
        if (principal.equals("anonymousUser"))
            return new ApiResponse("Xatolik", false);

        //BU YERGACHA KELGAN USER ALBATTA USER DETAILS DAN VORIS OLGAN BO'LISHI KERAK
        if (!(principal instanceof UserDetails))
            return new ApiResponse("Xatolik", false);

        User user = (User) principal;

        //CHECK UNIQUE EMAIL
        boolean exists = userRepository.existsByEmail(registerDto.getEmail());
        if (exists)
            return new ApiResponse("Ushbu emailli foydalanuvchi tizimda mavjud",false);


        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

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

        RoleName roleName = registerDto.getRoleName();

        //ADD WORKER //DIRECTOR VA MANAGER BO'LSAGINA WORKER QO'SHA OLADI
        if (roleName.equals(RoleName.ROLE_WORKER) && (roleNumber == 1 || roleNumber == 2))
            return saveUser(registerDto, RoleName.ROLE_WORKER);


        //ADD MANAGER  //MANAGER NI FAQAT DIRECTOR QO'SHISHI MUMKIN
        if (roleName.equals(RoleName.ROLE_MANAGER) && roleNumber == 2)
            return saveUser(registerDto, RoleName.ROLE_MANAGER);

        //XATOLIK  //
        return new ApiResponse("Xatolik", false);
    }





    //LOGIN
    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token",true,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("parol yoki login xato",false);
    }


    public ApiResponse verify(RegisterDto registerDto) {

        return null;
    }


    //METHOD FOR SAVE
    public ApiResponse saveUser(RegisterDto registerDto, RoleName roleName) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode("0000"));
        user.setRoles(Collections.singleton(roleRepository.getByName(roleName)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        mailService.sendEmail(user.getEmail(), user.getEmailCode(), true, "Emailni tasdiqlash");
        return new ApiResponse(roleName.name() + " added", true);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("username yoki parol xato"));
    }


}
