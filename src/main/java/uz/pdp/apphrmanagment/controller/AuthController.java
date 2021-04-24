package uz.pdp.apphrmanagment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.LoginDto;
import uz.pdp.apphrmanagment.payload.RegisterDto;
import uz.pdp.apphrmanagment.payload.VerifyDto;
import uz.pdp.apphrmanagment.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody @Valid RegisterDto registerDto){
        ApiResponse apiResponse = authService.register(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDto loginDto){
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    //VERIFY EMAIL YO'LIGA GET BILAN MUROJAAT BO'LGANDA FRONT GA
    // FORM CHIQARIB BERILADI VA VERIFY_DTO TURIDAGI MA'LUMOTLAR KIRITILIB POST BILAN JO'NATILADI

    @PostMapping("/verifyEmail")
    public HttpEntity<?> verify(@RequestParam String email, @RequestParam String emailCode,
                                @RequestBody @Valid VerifyDto verifyDto){
        ApiResponse apiResponse = authService.verify(email,emailCode,verifyDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

}
