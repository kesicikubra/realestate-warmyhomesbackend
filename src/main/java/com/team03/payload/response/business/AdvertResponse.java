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
public class AdvertResponse extends BaseAdvertResponse implements Serializable {

    private List<ImageResponse> images;

}
