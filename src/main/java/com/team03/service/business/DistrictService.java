package com.team03.service.business;

import com.team03.entity.business.District;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.DistrictsMapper;
import com.team03.payload.response.business.DistrictsResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictsMapper districtsMapper;


    public District getDistrictById(Long id){
        return districtRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.district", id)));
    }

   // @Cacheable(cacheNames = "district")
    public ResponseMessage<List<DistrictsResponse>> getDistricts() {
        List<District> districties = districtRepository.findAll();
        List<DistrictsResponse> districtsResponses = districties.stream()
                .map(districtsMapper::responseToDistricts)
                .collect(Collectors.toList());

        return ResponseMessage.<List<DistrictsResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(districtsResponses)
                .build();
    }
}
