package com.team03.payload.mappers;

import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;
import com.team03.payload.request.abstracts.BaseUserRequest;
import com.team03.payload.request.user.UserRequestForUpdate;
import com.team03.payload.response.user.*;

import com.team03.service.business.AdvertService;
import com.team03.service.business.FavoriteService;
import com.team03.service.business.LogService;
import com.team03.service.business.TourRequestService;
import com.team03.service.user.UserProfilePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {


    private final AdvertService advertService;
    private final LogService logService;
    private final TourRequestService tourRequestService;
    private final FavoriteService favoriteService;
    private final ImageMapper imageMapper;
    private final UserProfilePhotoService profilePhotoService;

    public User mapRegisterRequestToUser(BaseUserRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirst_name())
                .lastName(registerRequest.getLast_name())
                .phoneNumber(registerRequest.getPhone_number())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .builtIn(false)
                .createAt(LocalDateTime.now())
                .build();
    }

    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .phone_number(user.getPhoneNumber())
                .create_at(user.getCreateAt())
                .user_roles(convertRolesToStringSet(user))
                .photoResponse(profilePhotoService.getProfilePhoto(user))
                .build();
    }
    public UserResponseForUpdateAdmin mapUserToUserResponseWithRole (User user){
        return UserResponseForUpdateAdmin.builder()
                .id(user.getId())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .phone_number(user.getPhoneNumber())
                .create_at(user.getCreateAt())
                .update_at(user.getUpdateAt())
                .user_roles(convertRolesToStringSet(user))
                .photoResponse(profilePhotoService.getProfilePhoto(user))
                .build();
    }


    public UserResponseAllUsersInfo mapUserToUserResponseAllUserInfo(User user ,int page, int size, String sort, String type){

        return  UserResponseAllUsersInfo.builder()
                .id(user.getId())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .phone_number(user.getPhoneNumber())
                .create_at (user.getCreateAt ())
                .user_roles(convertRolesToStringSet(user))
                .updateAt (user.getUpdateAt ())
                .photoResponse(profilePhotoService.getProfilePhoto(user))
                .tourRequests (tourRequestService.getAuthenticatedUsersAllTourRequest (user,page,size,sort,type))
                .adverts (advertService.getAdvertsByUserId (user.getId (),page,size,sort,type))
                .favorites (favoriteService.getUserFavoritesByUserId (user.getId (),page,size,sort,type))
                .logs (logService.getByUserPage (user.getId (),page,size,sort,type).getObject ())
                .build();
    }


    public UpdatedUserResponse mapUpdatedUserToUserResponse(User user) {
        return UpdatedUserResponse.builder()
                .id(user.getId())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .phone_number(user.getPhoneNumber())
                .create_at(user.getCreateAt())
                .update_at(user.getUpdateAt())
                .user_roles(convertRolesToStringSet(user))
                .photoResponse(profilePhotoService.getProfilePhoto(user))
                .build();
    }

    public User mapUserUpdateRequestToUser (UserRequestForUpdate request, User user){
        return User.builder()
                .firstName(request.getFirst_name())
                .lastName(request.getLast_name())
                .email(request.getEmail())
                .phoneNumber(request.getPhone_number())
                .createAt(user.getCreateAt())
                .updateAt(LocalDateTime.now())
                .builtIn(user.getBuiltIn())
                .enabled (user.isEnabled())
                .profilePhoto (user.getProfilePhoto ())
                .userRoles(user.getUserRoles())
                .id(user.getId())
                .password(user.getPassword())
                .resetPasswordCode(user.getResetPasswordCode())
                .build();

    }
    public AuthResponse mapUserToAuthResponse(User user,String accessToken, String refreshToken){
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(convertRolesToStringSet(user))
                .first_name(user.getFirstName())
                .photoId(user.getProfilePhoto() != null ? user.getProfilePhoto().getId() : null)
                .build();
    }

    public static Set<String> convertRolesToStringSet(User user) {
        return user.getUserRoles().stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
    }

    public UserPhotoResponse toUserResponse(User user) {
        return UserPhotoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNumber ())
                .role(convertRolesToStringSet (user))
                .photoResponse(profilePhotoService.getProfilePhoto(user))
                .build();
    }


}
