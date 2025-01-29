package com.team03.controller.buiness;

import com.team03.payload.response.business.DistrictsResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
public class DistrictsController {

    private final DistrictService districtService;

    @GetMapping
    @Operation(tags = "ADDRESS" , summary = "U03")
    public ResponseMessage<List<DistrictsResponse>> getDistricties(){
        return districtService.getDistricts();
    }
}
