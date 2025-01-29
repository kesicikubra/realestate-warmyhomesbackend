package com.team03.service.business;

import com.team03.entity.business.City;
import com.team03.entity.business.Country;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.CityNameMapper;
import com.team03.payload.response.business.CityAmountResponse;
import com.team03.payload.response.business.CityNameResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.CityRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CityNameMapper cityNameMapper;

    public City getCityById(Long id){
        return cityRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.city", id)));
    }

    public City getCountryByCityId(Long cityId, Long districtId) {

        boolean exists = cityRepository.existsByDistrictId(cityId, districtId);
        if (!exists){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.city.by.district.id", districtId));
        }
        return getCityById(cityId);
    }

 //   @Cacheable(cacheNames = "cities")
    public ResponseMessage<List<CityNameResponse>> getCities() { //
        List<City> cities = cityRepository.findAll();

        List<CityNameResponse> cityNameResponses = cities.stream()
                .map(cityNameMapper::responseToCity)
                .collect(Collectors.toList());
        /*cityreposti.findall()*/

        return ResponseMessage.<List<CityNameResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(cityNameResponses)
                .build();
    }

}


















