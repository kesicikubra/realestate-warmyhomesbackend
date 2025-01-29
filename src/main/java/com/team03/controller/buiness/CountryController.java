package com.team03.controller.buiness;

import com.team03.payload.response.business.CountryResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    @Operation(tags = "ADDRESS" , summary = "U01")
    public ResponseMessage<List<CountryResponse>> getCountries(){
        return countryService.getCountries();
    }
}
