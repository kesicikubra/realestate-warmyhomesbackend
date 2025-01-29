package com.team03.payload.mappers;

import com.team03.entity.business.District;
import com.team03.payload.response.business.DistrictsResponse;
import org.springframework.stereotype.Component;

@Component
public class DistrictsMapper {
    public DistrictsResponse responseToDistricts(District district) {
        if (district == null) {
            return null;
        }

        String cityName = district.getCity() != null ? district.getCity().getName() : null;
        Long cityId = district.getCity() != null ? district.getCity().getId() : null;

        return DistrictsResponse.builder()
                .id(district.getId())
                .cityName(cityName)
                .cityId(cityId)
                .districtsName(district.getName())
                .build();
    }
}
