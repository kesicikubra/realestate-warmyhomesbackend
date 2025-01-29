package com.team03.controller.buiness;


import com.team03.payload.response.business.CityNameResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.CityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping()
    @Operation(tags = "ADDRESS" , summary = "U02")
    public ResponseMessage<List<CityNameResponse>> getCities() {
        return cityService.getCities();
    }


}
