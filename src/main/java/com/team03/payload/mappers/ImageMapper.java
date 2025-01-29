package com.team03.payload.mappers;

import com.team03.entity.business.Advert;
import com.team03.entity.business.Image;
import com.team03.entity.user.ProfilePhoto;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.ImageResponse;
import com.team03.payload.response.user.PhotoResponse;
import com.team03.service.helper.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.DataFormatException;

@Component
@RequiredArgsConstructor
public class ImageMapper {

    public ImageResponse imageToImageResponse(Image image){
        try {
            return ImageResponse.builder()
                    .id(image.getId())
                    .imageData(ImageUtils.decompressImage(image.getImageData()))
                    .type(image.getType())
                    .name(image.getName())
                    .featured(image.getFeatured())
                    .build();
        } catch (IOException | DataFormatException exception) {
            throw new ContextedRuntimeException(MessageUtil.getMessage("decompressing.image"));
        }
    }

    public Image imageRequestToImage (MultipartFile file, Boolean featured, Advert advert,Boolean suitable){
        try {
            return Image.builder()
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                    .type(file.getContentType())
                    .featured(featured)
                    .suitable(suitable)
                    .advert(advert)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProfilePhoto toProfilePhoto(MultipartFile file){

        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            return  ProfilePhoto.builder()
                      .data (ImageUtils.compressImage (file.getBytes ()))
                      .name(fileName)
                      .type (file.getContentType ())
                      .build();
        } catch (IOException e) {
            throw new ContextedRuntimeException(MessageUtil.getMessage("compressing.image"));
        }

    }

    public PhotoResponse toPhotoResponse(ProfilePhoto profilePhoto) {
        byte[] photo;
        try {
            photo = ImageUtils.decompressImage(profilePhoto.getData());
        } catch (DataFormatException | IOException e) {
            throw new ContextedRuntimeException(MessageUtil.getMessage("decompressing.image"));
        }
        return PhotoResponse.builder()
                .id(profilePhoto.getId())
                .name(profilePhoto.getName())
                .type(profilePhoto.getType())
                .data(photo)
                .build();
    }


}