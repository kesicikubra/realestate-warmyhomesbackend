package com.team03.payload.request.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImageRequest  implements Serializable {

    @NotNull(message = "{image.request.image.notnull}")
    @NotBlank(message = "{image.request.image.notblank}")
    private MultipartFile image;

    @NotNull(message = "{image.request.featured.notnull}")
    private Boolean featured;


}
