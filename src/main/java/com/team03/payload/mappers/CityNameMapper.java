package com.team03.payload.mappers;

import com.team03.entity.business.City;
import com.team03.payload.response.business.CityNameResponse;
import org.springframework.stereotype.Component;


@Component
public class CityNameMapper {

    public CityNameResponse responseToCity(City city) {
        if (city == null || city.getCountry() == null) {
            return null;
        }
        return CityNameResponse.builder()
                .id(city.getId())
                .countryId (city.getCountry ().getId ())
                .countryName(city.getCountry().getName())
                .cityName(city.getName())
                .build();
    }
}
