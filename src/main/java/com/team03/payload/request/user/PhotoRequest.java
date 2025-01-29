package com.team03.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PhotoRequest implements Serializable {

    @NotNull(message = "{image.request.image.notnull}")
    @NotBlank(message = "{image.request.image.notblank}")
    private MultipartFile photo;
}
