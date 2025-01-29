package com.team03.service.user;

import com.team03.entity.enums.RoleType;
import com.team03.entity.user.ProfilePhoto;
import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;
import com.team03.exception.BadRequestException;
import com.team03.exception.ConflictException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.ImageMapper;
import com.team03.payload.mappers.UserMapper;
import com.team03.payload.request.user.*;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.user.*;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.FavoriteRepository;
import com.team03.repository.business.LogRepository;
import com.team03.repository.business.TourRequestRepository;
import com.team03.repository.user.UserRepository;
import com.team03.service.helper.MethodHelper;
import com.team03.service.helper.RestPage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final MethodHelper helper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AdvertRepository advertRepository;
    private final TourRequestRepository tourRequestRepository;
    private final FavoriteRepository favoriteRepository;
    private final LogRepository logRepository;
    private final UserRoleService userRoleService;
    private final RefreshTokenService refreshTokenService;
    private final UserProfilePhotoService userProfilePhotoService;
    private final UserProfilePhotoService photoService;
    private final ImageMapper imageMapper;
    private final VerificationTokenService verificationTokenService;



    public ResponseMessage<String> updatePassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest(request);

        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        if (!passwordEncoder.matches(updatePasswordRequest.getOld_password(), user.getPassword())) {
            throw new BadRequestException(MessageUtil.getMessage ("password.not.matched"));
        }

        String encodedPassword = passwordEncoder.encode(updatePasswordRequest.getNew_password());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("updated.password"))
                .build();
    }

    public ResponseMessage<UpdatedUserResponse> updateUser(UserRequestForUpdate userRequest, HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest(request);
        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        String email = userRequest.getEmail();
        String phoneNumber = userRequest.getPhone_number();

        User emailUser = userRepository.findUserByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.user.message")));

        if (!Objects.isNull(emailUser) && !user.getId().equals(emailUser.getId())){
            throw new ConflictException(MessageUtil.getMessage ("already.exists.user.email",email));
        }

        User phoneUser = userRepository.findByPhoneNumber(phoneNumber);
        if (!Objects.isNull(phoneUser) && !user.getId().equals(phoneUser.getId())){
            throw new ConflictException(MessageUtil.getMessage ("already.exists.user.phonenumber",phoneNumber));
        }

        User updatedUser = userMapper.mapUserUpdateRequestToUser(userRequest, user);

        user.setEnabled (true);
        user.setProfilePhoto (user.getProfilePhoto ());
        return ResponseMessage.<UpdatedUserResponse>builder()
                .object(userMapper.mapUpdatedUserToUserResponse(userRepository.save(updatedUser)))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("user.update.message"))
                .build();
    }

    // @Cacheable(value = "user")
    public ResponseMessage<UserResponse> getUserInfo(HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest(request);

        return ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(user))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //@Cacheable(value = "users",key = "#q")
    public ResponseMessage<Page<UserResponse>> getAllUsersByPage(String q, int page, int size, String sort, String type) {

        Pageable pageable= helper.getPageableWithProperties(page,size,sort,type);
        Page<User> usersPage= userRepository.findBySearchQuery(q, pageable);
        Page<UserResponse> userResponses = usersPage.map(userMapper::mapUserToUserResponse);
        RestPage<UserResponse>userResponses1=new RestPage<>(userResponses);

        return ResponseMessage.<Page<UserResponse>>builder()
                .object(userResponses1)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //  @Cacheable(value = "users",key = "#id")
    public ResponseMessage<RestPage<UserResponseAllUsersInfo>> getUserById(Long id,int page, int size, String sort, String type) {

        User user = findUserById(id);
        UserResponseAllUsersInfo usersInfo=userMapper.mapUserToUserResponseAllUserInfo(user,page,size,sort,type);

        List<UserResponseAllUsersInfo> userInfoList = Collections.singletonList (usersInfo);
        RestPage<UserResponseAllUsersInfo>usersInfos=new RestPage<> (userInfoList,page,size,1);

        return ResponseMessage.<RestPage<UserResponseAllUsersInfo>>builder()
                .object(usersInfos)
                .message(MessageUtil.getMessage ("user.found",id))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<UserResponseForUpdateAdmin> updatesAdminUser(UserRequestForUpdateRole userRequest, Long userId, HttpServletRequest request) {

        User user = findUserById(userId);
        String email = (String) request.getAttribute("email");

        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            return ResponseMessage.<UserResponseForUpdateAdmin>builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message(MessageUtil.getMessage ("not.permitted.method.message"))
                    .build();
        }
        // TODO: bu role zaten sahipse exception firlatabilir
        Set<String> roles = userRequest.getUser_roles();
        Set<UserRole> recordRole = new HashSet<>();

        if (!checkUserRoleForAdmin(email, RoleType.ADMIN)) {
            if (roles.size() > 1 || roles.stream().noneMatch(t -> t.equalsIgnoreCase(RoleType.CUSTOMER.getName()))) {
                return ResponseMessage.<UserResponseForUpdateAdmin>builder()
                        .message(MessageUtil.getMessage ("not.permitted.method"))
                        .httpStatus(HttpStatus.UNAUTHORIZED).build();
            }
        }

        for (String role : roles) {
            if (role.equalsIgnoreCase(RoleType.ADMIN.getName())){
                recordRole.add(userRoleService.getUserRole(RoleType.ADMIN));
            } else if (role.equalsIgnoreCase(RoleType.MANAGER.getName())) {
                recordRole.add(userRoleService.getUserRole(RoleType.MANAGER));
            } else if (role.equalsIgnoreCase(RoleType.CUSTOMER.getName())){
                recordRole.add(userRoleService.getUserRole(RoleType.CUSTOMER));
            } else {
                throw new BadRequestException(String.format(MessageUtil.getMessage ("not.found.user.role",role)));
            }
        }

        user.setUserRoles(recordRole);
        user.setUpdateAt(LocalDateTime.now());

        return ResponseMessage.<UserResponseForUpdateAdmin>builder()
                .object(userMapper.mapUserToUserResponseWithRole(userRepository.save(user)))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("user.update.message"))
                .build();
    }

    //  @CacheEvict(value = "user",key = "#userId")
    public ResponseMessage<UserResponse> deleteById(Long userId, HttpServletRequest request) {

        User userExist = findUserById(userId);
        Set<UserRole> roles = userExist.getUserRoles();
        String email = (String) request.getAttribute("email");

        if (Boolean.TRUE.equals(userExist.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method"));
        }

        if (!checkUserRoleForAdmin(email, RoleType.ADMIN)) {
            // kullanici manager ise customer disindakileri silemez
            if (roles.size()>1 || !checkUserRoleForAdmin(userExist.getEmail(), RoleType.CUSTOMER)){
                throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
            }
        }
        Long id = userExist.getId();
        checkAdvertAndTourRequest(id);

        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("user.deleted.message"))
                .object(userMapper.mapUserToUserResponse(userExist))
                .build();
    }

    // @CacheEvict(value = "user")
    public ResponseMessage<UserResponse> deleteAccount (PasswordRequest password){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.user.message")));

        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        if (!passwordEncoder.matches(password.getPassword(), user.getPassword())) {
            throw new BadRequestException(MessageUtil.getMessage ("user.password.message"));
        }

        checkAdvertAndTourRequest(user.getId());


        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage ("user.deleted.message"))
                .object(userMapper.mapUserToUserResponse(user))
                .build();
    }

    public Boolean checkUserRoleForAdmin(String email, RoleType roleType) {
        return userRepository.existUserByRole(email, roleType);
    }
    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(MessageUtil.getMessage ("not.found.user",id)));
    }
    private void checkAdvertAndTourRequest(Long userId) {

        if (!advertRepository.existsByUserId(userId) && !tourRequestRepository.existsByUserId(userId)) {
            verificationTokenService.deleteByUserId(userId);
            favoriteRepository.deleteByUserId(userId);
            logRepository.deleteByUserId(userId);
            refreshTokenService.deleteRefreshTokenByUserId(userId);
            userRepository.deleteById(userId);
        } else {
            throw new BadRequestException(MessageUtil.getMessage ("user.cannot.be.deleted.message"));
        }
    }




    public ResponseMessage< UserPhotoResponse >addOrUpdatePhoto(MultipartFile  photoRequest, HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest (request);

        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        ProfilePhoto profilePhoto;
        if (user.getProfilePhoto () == null) {
            profilePhoto =photoService.uploadUserProfilePhoto (photoRequest);
        }else {
            profilePhoto =photoService.updateUserProfilePhoto (user.getProfilePhoto (),photoRequest);
        }
        user.setProfilePhoto(profilePhoto);

        User updated =  userRepository.save(user);

        return ResponseMessage.<UserPhotoResponse>builder ()
                .httpStatus(HttpStatus.OK)
                .object (userMapper.toUserResponse (updated))
                .build ();
    }



    public ResponseMessage<UserPhotoResponse> addPhoto(MultipartFile photoRequest, HttpServletRequest request) {
        User user = helper.getUserByHttpServletRequest(request);

        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        ProfilePhoto profilePhoto = photoService.uploadUserProfilePhoto(photoRequest);
        user.setProfilePhoto(profilePhoto);
        user.setUpdateAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        return ResponseMessage.<UserPhotoResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(userMapper.toUserResponse(updatedUser))
                .build();
    }



    public ResponseMessage<UserPhotoResponse> updatePhoto(MultipartFile photoRequest, HttpServletRequest request) {
        User user = helper.getUserByHttpServletRequest(request);


        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }

        if (user.getProfilePhoto() == null) {
            throw new BadRequestException("User does not have a profile photo to update.");
        }

        ProfilePhoto updatedProfilePhoto = photoService.updateUserProfilePhoto(user.getProfilePhoto(), photoRequest);
        user.setProfilePhoto(updatedProfilePhoto);
        user.setUpdateAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        return ResponseMessage.<UserPhotoResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(userMapper.toUserResponse(updatedUser))
                .build();
    }



    public ResponseMessage<UserPhotoResponse> deletePhoto( HttpServletRequest request) {
        User user = helper.getUserByHttpServletRequest (request);
        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
        }
        photoService.deleteUserProfilePhoto (user.getProfilePhoto ());
        user.setProfilePhoto(null);
        user.setUpdateAt(LocalDateTime.now());
        User updated =  userRepository.save(user);
        return ResponseMessage.<UserPhotoResponse>builder ()
                .object (userMapper.toUserResponse (updated))
                .httpStatus (HttpStatus.OK)
                .build ();
    }


    public ResponseMessage<UserPhotoResponse>getPhotoById(Long photoId){
        User user = userRepository.findPhotoById(photoId).orElseThrow(
                () -> new ResourceNotFoundException (MessageUtil.getMessage ("not.found.image.by.id",photoId)));
        return ResponseMessage.<UserPhotoResponse>builder ()
                .httpStatus (HttpStatus.OK)
                .object(userMapper.toUserResponse (user))
                .build ();
    }



    public void deleteUser(List< User> users){
        for (User user : users) {
            logRepository.deleteByUserId (user.getId ());
            favoriteRepository.deleteByUserId (user.getId ());
            refreshTokenService.deleteRefreshTokenByUserId (user.getId ());
            advertRepository.deleteById (user.getId ());

            userRepository.delete(user);
        }
        ResponseMessage.< String >builder ()
                .httpStatus (HttpStatus.OK)
                .message (MessageUtil.getMessage ("user.deleted.message"))
                .build ();


    }
}
