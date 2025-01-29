package com.team03.payload.response.business;

import com.team03.payload.response.abstracts.BaseAdvertResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertResponseWithFavorited extends BaseAdvertResponse {

    private byte[] featured_image;
    private boolean favorited_advert;

}
