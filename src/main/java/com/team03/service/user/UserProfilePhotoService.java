package com.team03.service.user;


import com.team03.entity.user.ProfilePhoto;
import com.team03.entity.user.User;
import com.team03.payload.mappers.ImageMapper;
import com.team03.payload.response.user.PhotoResponse;
import com.team03.repository.user.UserProfilePhotoRepository;
import com.team03.service.helper.ImageUtils;
import com.team03.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfilePhotoService {
    private final UserProfilePhotoRepository userProfilePhotoRepository;
    private final ImageMapper imageMapper;
    private final MethodHelper methodHelper;

    public ProfilePhoto uploadUserProfilePhoto(MultipartFile file) {

        methodHelper.isImageEmpty(file);
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            ProfilePhoto photo=  ProfilePhoto.builder()
                    .data (ImageUtils.compressImage (file.getBytes ()))
                    .name(fileName)
                    .type (file.getContentType ())
                    .build();

            return userProfilePhotoRepository.save (photo);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    public ProfilePhoto updateUserProfilePhoto(ProfilePhoto profilePhoto, MultipartFile file) {
        methodHelper.isImageEmpty(file);
        ProfilePhoto requestPhoto =imageMapper.toProfilePhoto(file);
        requestPhoto.setId(profilePhoto.getId());
        return userProfilePhotoRepository.save(requestPhoto);
    }

    public void deleteUserProfilePhoto(ProfilePhoto profilePhoto) {
        userProfilePhotoRepository.deleteById (profilePhoto.getId ());
    }

    public PhotoResponse getProfilePhoto(User user){
        Optional<ProfilePhoto> profilePhoto = userProfilePhotoRepository.findByUser(user);
        return profilePhoto.map(imageMapper::toPhotoResponse).orElse(null);
    }

}
