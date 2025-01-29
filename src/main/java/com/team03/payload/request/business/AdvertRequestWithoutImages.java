package com.team03.payload.request.business;

import com.team03.payload.request.abstracts.BaseAdvertRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertRequestWithoutImages extends BaseAdvertRequest {

    @NotNull(message = "{advert.request.update.without.images.isactive}")
    private boolean is_active;

}
