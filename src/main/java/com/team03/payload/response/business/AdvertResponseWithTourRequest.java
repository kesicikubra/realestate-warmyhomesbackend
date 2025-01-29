package com.team03.payload.response.business;

import com.team03.payload.response.abstracts.BaseAdvertResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertResponseWithTourRequest extends BaseAdvertResponse implements Serializable {

    private List<PropertyResponse> properties;
    private List<ImageResponse> images;
    private List<TourRequestResponseForAdvert> tourRequest;
    private Long country_id;
    private Long city_id;
    private Long district_id;
    private String category_title;
    private Long category_id;
    private Long owner_user_id;

}
