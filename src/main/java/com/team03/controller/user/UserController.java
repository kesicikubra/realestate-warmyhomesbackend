package com.team03.controller.user;



import com.team03.payload.request.user.*;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.user.*;
import com.team03.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {


    private  final UserService userService;

    // F01 http://localhost:8081/users/login
    @PostMapping("/login")
    @Operation(tags = "Authentication", summary = "F01")
    public ResponseMessage<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return userService.login(loginRequest);
    }


    // F02
    @PostMapping("/register") // http://localhost:8080/register
    @Operation(tags = "Authentication", summary = "F02")
    public ResponseMessage<UserResponse> register(@RequestBody @Valid UserRegisterRequest registerRequest){
        return userService.registerNewUserAccount(registerRequest);
    }

    // F03
    @PostMapping("/forgot-password")
    @Operation(tags = "Authentication", summary = "F03")
    public ResponseMessage<String>forgotPassword(@RequestBody @Valid ForgotPasswordRequest email){
        return userService.forgotPassword(email);
    }

    // F04
    @PostMapping("/reset-password")
    @Operation(tags = "Authentication", summary = "F04")
    public ResponseMessage<String>resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){

        return userService.resetPassword(resetPasswordRequest);

    }


    @PostMapping("/refreshToken")
    @Operation(tags = "REFRESH TOKEN")
    public ResponseMessage<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.refreshToken(refreshTokenRequest);
    }

    @GetMapping("/verify")
    @Operation(tags = "Authentication", summary = "verification")
    public ResponseMessage<UserResponse> confirmRegistration ( @RequestParam("token") String token, HttpServletResponse response) {

        return userService.confirmRegistration(token,response);

    }
}
