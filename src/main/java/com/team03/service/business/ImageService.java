package com.team03.service.business;


import com.team03.ai.PhotoController;
import com.team03.entity.business.Advert;
import com.team03.entity.business.Image;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.ImageMapper;
import com.team03.payload.request.business.ImageRequest;
import com.team03.payload.request.business.ImageRequestUpload;
import com.team03.payload.response.business.ImageResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.ImageRepository;
import com.team03.service.helper.ImageUtils;
import com.team03.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final MethodHelper methodHelper;
    private final ImageMapper imageMapper;
    private final PhotoController controller;

    public List<Image> uploadImage(List<ImageRequest> images, Advert advert) {

        List<Image> imageList = new ArrayList<>();

        for (ImageRequest file : images) {
            MultipartFile imageFile = file.getImage();
            methodHelper.isImageEmpty(imageFile);
            Boolean featured = file.getFeatured();

            boolean isPhotoSuitable = controller.checkPhotoSuitability(imageFile);

            Image image = imageMapper.imageRequestToImage(imageFile,featured,advert,isPhotoSuitable);
            imageRepository.save(image);
            imageList.add(image);
        }
        return imageList;
    }

    public ResponseMessage<ImageResponse> getImageId(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.image.by.id", imageId)));
        return ResponseMessage.<ImageResponse>builder()
                .object(imageMapper.imageToImageResponse(image))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<List<ImageResponse>> uploadImageList(Long advertId, ImageRequestUpload files) {

        Advert advert = methodHelper.getAdvertById(advertId);
        List<Image> images = imageRepository.findByAdvertId(advertId);
        List<ImageRequest> imageRequests = files.getImageRequestList();
        long featuredCount = imageRequests.stream()
                .filter(imageRequest -> {
                    methodHelper.isImageEmpty(imageRequest.getImage());
                    return imageRequest.getFeatured();
                })
                .count();
        long dbFeaturedCount = images.stream()
                .filter(Image::getFeatured)
                .count();

        if (featuredCount > 1) {
            throw new BadRequestException(MessageUtil.getMessage("image.should.be.one.featured"));
        } else if (dbFeaturedCount >= 1 && featuredCount == 1) {
            throw new BadRequestException(MessageUtil.getMessage("already.exists.featured.image"));
        }

        List<Image> updatedImages = uploadImage(files.getImageRequestList(), advert);

        List<ImageResponse> imageResponses = updatedImages.stream()
                .map(imageMapper::imageToImageResponse)
                .toList();

        return ResponseMessage.<List<ImageResponse>>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(MessageUtil.getMessage("image.created.message"))
                .object(imageResponses)
                .build();
    }

    public ResponseMessage<String> deleteImages(String imageIds) {

        if (Objects.isNull(imageIds) || imageIds.isEmpty()) {
            return ResponseMessage.<String>builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageUtil.getMessage("image.empty"))
                    .build();
        }

        List<Long> idList = Arrays.stream(imageIds.split(","))
                .map(Long::parseLong)
                .toList();

        List<Long> advertIds = idList.stream()
                .map(id -> {
                    Image image = imageRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.image.by.id", id)));
                    if (image.getFeatured()) {
                        throw new BadRequestException(MessageUtil.getMessage("featured.image.delete"));
                    }
                    return image.getAdvert().getId();
                })
                .distinct()
                .toList();

        if (advertIds.size() != 1) {
            throw new BadRequestException(MessageUtil.getMessage("image.ids.different.advert"));
        }

        imageRepository.deleteAllByIdIn(idList);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("images.deleted.message"))
                .build();
    }

    public ResponseMessage<ImageResponse> updateImage(Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (optionalImage.isEmpty()) {
            return ResponseMessage.<ImageResponse>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(MessageUtil.getMessage("not.found.image.by.id", id))
                    .build();
        }
        Image image = optionalImage.get();

        if (image.getFeatured()) {
            throw new BadRequestException(MessageUtil.getMessage("image.already.featured.true"));
        }

        List<Image> imagesToUpdate = imageRepository.findByAdvertIdAndIsFeaturedTrue(image.getAdvert().getId());
        for (Image img : imagesToUpdate) {
            img.setFeatured(false);
        }
        imageRepository.saveAll(imagesToUpdate);
        image.setFeatured(true);

        return ResponseMessage.<ImageResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("image.updated.message"))
                .object(imageMapper.imageToImageResponse(imageRepository.save(image/*imageMapper.imageToImage(image)*/)))
                .build();
    }

    public List<ImageResponse> findImagesByAdvertId(Long advertId) {
        List<Image> images = imageRepository.findByAdvertId(advertId);
        if (images.isEmpty()) {
            return null;
        }
        List<ImageResponse> imageResponses = new ArrayList<>();
        for (Image image : images) {
            imageResponses.add(imageMapper.imageToImageResponse(image));
        }
        return imageResponses;
    }

    public byte[] findFeaturedImageByAdvertId(Long advertId) {
        List<Image> images = imageRepository.findByAdvertId(advertId);

        if (images.isEmpty ()){
            return null;
        }

        for (Image image : images) {
            if (image.getFeatured()){
                try {
                    return ImageUtils.decompressImage(image.getImageData());
                } catch (IOException | DataFormatException exception) {
                    throw new ContextedRuntimeException(MessageUtil.getMessage("decompressing.image"));
                }
            }
        }

        return null;
    }

    public void updateImageSuitable(Long advertId) {
        List<Image> images = imageRepository.findByAdvertId(advertId);
        if (images.isEmpty()){
            return;
        }
        for (Image image: images){
            image.setSuitable(true);
        }
        imageRepository.saveAll(images);
    }
}