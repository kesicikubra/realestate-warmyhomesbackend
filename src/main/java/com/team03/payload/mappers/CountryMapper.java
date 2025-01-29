package com.team03.payload.mappers;


import com.team03.entity.business.Country;
import com.team03.payload.response.business.CountryResponse;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    public CountryResponse responseToCountry (Country country){
        return CountryResponse.builder()
                .id(country.getId())
                .countryName(country.getName())
                .build();
    }
}
