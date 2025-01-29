package com.team03.payload.response.business;

import com.team03.payload.response.abstracts.BaseAdvertResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertWithPropertiesResponse extends BaseAdvertResponse implements Serializable {

    private List<PropertyResponse> properties;
    private List<ImageResponse> images;

}
