package com.team03.controller.buiness;

import com.team03.payload.request.business.ImageRequestUpload;
import com.team03.payload.response.business.ImageResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{advertId}")
    @Operation(tags = "Image",summary = "I02",description = "ADMIN MANAGER CUSTOMER'")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    public ResponseMessage<List<ImageResponse>> uploadImage(@PathVariable Long advertId, @ModelAttribute @Valid ImageRequestUpload file) {
        return imageService.uploadImageList(advertId, file);
    }

    @GetMapping("/{imageId}")
    @Operation(tags = "Image",summary = "I01")
    public ResponseMessage<ImageResponse> getImageId (@PathVariable Long imageId){
        return imageService.getImageId(imageId);
    }

    @DeleteMapping("/{image_ids}")
    @Operation(tags = "Image",summary = "I03")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    public ResponseMessage<String> deleteImages (@PathVariable(name = "image_ids") String imageIds){
        return imageService.deleteImages(imageIds);
    }

    @PutMapping("/{imageId}")
    @Operation(tags = "Image",summary = "I04")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    public ResponseMessage<ImageResponse> updateImage (@PathVariable Long imageId){
        return imageService.updateImage(imageId);
    }
}
