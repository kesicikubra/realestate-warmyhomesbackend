package com.team03.payload.response.business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CityNameResponse implements Serializable {

    private Long id;
    private Long countryId;

    private String cityName;
    private String countryName;

}
