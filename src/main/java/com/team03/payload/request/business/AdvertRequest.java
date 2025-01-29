package com.team03.payload.request.business;

import com.team03.payload.request.abstracts.BaseAdvertRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertRequest extends BaseAdvertRequest implements Serializable {

    private List<ImageRequest> images;

}
