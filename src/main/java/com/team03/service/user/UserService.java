package com.team03.service.user;

import com.team03.entity.enums.RoleType;
import com.team03.entity.user.RefreshToken;
import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;

import com.team03.entity.user.VerificationToken;
import com.team03.exception.BadRequestException;
import com.team03.exception.ConflictException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.UserMapper;
import com.team03.payload.request.user.*;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.user.*;
import com.team03.repository.user.UserRepository;
import com.team03.repository.user.VerificationTokenRepository;
import com.team03.security.jwt.JwtUtils;
import com.team03.service.email.EmailService;
import com.team03.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper helper;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenService verificationTokenService;

    @Value("${SERVER_DOMAIN_WARMYHOMES}")
    String baseurl;

    @Value("${FRONTEND_DOMAIN_LOGIN}")
    String loginurl;

    public ResponseMessage<UserResponse> registerUser(UserRegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        String phoneNumber = registerRequest.getPhone_number();

        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new ConflictException(MessageUtil.getMessage ("already.exists.user.email",email));
        }
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(phoneNumber))){
            throw new ConflictException(MessageUtil.getMessage ("already.exists.user.phonenumber",phoneNumber));
        }

        User user = userMapper.mapRegisterRequestToUser(registerRequest);
        Set<UserRole>roles = new HashSet<>();
        roles.add(userRoleService.getUserRole(RoleType.CUSTOMER));
        user.setUserRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registerUser=userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(MessageUtil.getMessage ("user.created"))
                .httpStatus(HttpStatus.CREATED)
                .object(userMapper.mapUserToUserResponse(registerUser))
                .build();
    }

    public ResponseMessage<AuthResponse> login(LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!authentication.isAuthenticated()) {
            throw new BadRequestException(MessageUtil.getMessage ("not.found.email.or.password"));
        }
        String accessToken = jwtUtils.generateJwtToken(loginRequest.getEmail());
        User user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.user.message")));

        if (!user.isEnabled()){
            return ResponseMessage.<AuthResponse>builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageUtil.getMessage ("user.not.enable"))
                    .build();
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


        return ResponseMessage.<AuthResponse>builder()
                .object(userMapper.mapUserToAuthResponse(user, accessToken, refreshToken.getToken()))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("login"))
                .build();

    }

    public ResponseMessage<String> forgotPassword(ForgotPasswordRequest request) {
        if (request.getEmail().trim().isEmpty()) {
            return helper.buildResponseMessage(MessageUtil.getMessage ("empty.email"), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.user.message")));


        String code = generateResetPasswordCode();
        user.setResetPasswordCode(code);
        userRepository.save(user);

        if (emailService.sendResetCodeEmail(user.getEmail(), user.getFirstName(),code)) {
            return helper.buildResponseMessage(MessageUtil.getMessage ("code.sent.to.email"), HttpStatus.OK);
        } else {
            user.setResetPasswordCode(null);
            userRepository.save(user);
            return  helper.buildResponseMessage(MessageUtil.getMessage ("mail.sending.error.message"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseMessage<String> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        // reset code sahip useri getir
        Optional<User> optionalUser = userRepository.findByResetPasswordCode(resetPasswordRequest.getCode());

        // user null kontrolü ve reset code kontrol et
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            // gelen yeni sifreyi setle ve dbye kaydet
            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
            user.setResetPasswordCode(null);
            userRepository.save(user);

            return helper.buildResponseMessage(MessageUtil.getMessage ("updated.password"),HttpStatus.OK);
        }

        return helper.buildResponseMessage(MessageUtil.getMessage ("not.match"),HttpStatus.BAD_REQUEST);
    }


    // reset kod olustur
    private String generateResetPasswordCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    // runner icin olusturulan methodlar

    public long countAllAdmins(){
        return userRepository.countAdmin(RoleType.ADMIN);
    }


    public void saveAdmin(User user) {
        Set<UserRole> role = new HashSet<>();
        role.add(userRoleService.getUserRole(RoleType.ADMIN));
        user.setUserRoles(role);
        user.setBuiltIn(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> findAllUsersByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    public ResponseMessage<JwtResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = refreshTokenService.findByToken(refreshTokenRequest.getRefresh_token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtUtils.generateJwtToken(userInfo.getEmail());
                    return JwtResponse.builder()
                            .access_token(accessToken)
                            .token(refreshTokenRequest.getRefresh_token())
                            .first_name(userInfo.getFirstName())
                            .last_name(userInfo.getLastName())
                            .build();
                }).orElseThrow(() -> new RuntimeException(MessageUtil.getMessage ("expired.token")));
        return ResponseMessage.<JwtResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(jwtResponse)
                .build();
    }
    public void saveCustomer(User user) {
        Set<UserRole> role = new HashSet<>();
        role.add(userRoleService.getUserRole(RoleType.CUSTOMER));
        user.setUserRoles(role);
        user.setBuiltIn(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public long countAllCustomers() {
        return userRepository.countCustomer(RoleType.CUSTOMER);
    }
    public ResponseMessage<UserResponse> registerNewUserAccount(UserRegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String phoneNumber = registerRequest.getPhone_number();

        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new ConflictException(String.format(MessageUtil.getMessage("already.exists.user.email",email)));
        }
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(phoneNumber))){
            throw new ConflictException(String.format(MessageUtil.getMessage("already.exists.user.phonenumber",phoneNumber)));
        }

        User user = userMapper.mapRegisterRequestToUser(registerRequest);
        Set<UserRole>roles = new HashSet<>();
        roles.add(userRoleService.getUserRole(RoleType.CUSTOMER));
        user.setUserRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEnabled(false);
        String token = UUID.randomUUID().toString();
        User registerUser=userRepository.save(user);
        VerificationToken verificationToken=crateVerificationToken(user,token);



        String url=baseurl;//deploy ile degisecek

        emailService.sendVerificationEmail(user.getEmail(),url, user.getFirstName(), verificationToken.getToken());

        return ResponseMessage.<UserResponse>builder()
                .message(MessageUtil.getMessage("user.verify"))
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(registerUser))
                .build();
    }

    private VerificationToken crateVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(myToken);
        return myToken;
    }

    public ResponseMessage<UserResponse> confirmRegistration(String token, HttpServletResponse response) {
        VerificationToken verifyToken= verificationTokenRepository.findByToken(token);

        if (verifyToken.getExpiryDate().isAfter(LocalDateTime.now())){
            User user= verifyToken.getUser();
            user.setEnabled(true);

            User registerUser= userRepository.save(user);
            try {
                response.sendRedirect(loginurl); // Login sayfasına yönlendir

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return ResponseMessage.<UserResponse>builder()
                .message(MessageUtil.getMessage("user.cannot.verify"))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
}
