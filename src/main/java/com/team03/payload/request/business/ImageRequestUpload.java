package com.team03.payload.request.business;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImageRequestUpload implements Serializable {

    @NotNull(message = "{image.request.upload.image.request.list.notnull}")
    private List<ImageRequest> imageRequestList;

}
