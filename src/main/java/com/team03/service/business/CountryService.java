package com.team03.service.business;

import com.team03.entity.business.Country;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.CountryMapper;
import com.team03.payload.response.business.CountryResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

  //  @Cacheable(value = "country",key = "#id")
    public Country getCountryById(Long id){
        return countryRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.country", id)));
    }


    public Country getCountryByCityId(Long countryId, Long cityId) {

        boolean exists = countryRepository.existsByCityId(countryId, cityId);
        if (!exists){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.country.by.city.id", countryId));
        }
        return getCountryById(countryId);
    }

  //  @Cacheable(cacheNames = "country")
    public ResponseMessage<List<CountryResponse>> getCountries() {
        List<Country> countries = countryRepository.findAll();
        List<CountryResponse> countryResponses = countries.stream()
                .map(countryMapper::responseToCountry)
                .collect(Collectors.toList());

        return ResponseMessage.<List<CountryResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(countryResponses)
                .build();
    }
}
