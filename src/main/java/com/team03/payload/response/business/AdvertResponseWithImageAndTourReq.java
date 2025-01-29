package com.team03.payload.response.business;

import com.team03.payload.response.abstracts.BaseAdvertResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvertResponseWithImageAndTourReq extends BaseAdvertResponse implements Serializable {

    private byte[] featured_image;
    private int favorite_number;
    private int tour_request_number;

}
