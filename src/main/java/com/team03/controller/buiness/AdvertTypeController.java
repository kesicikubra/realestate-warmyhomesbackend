package com.team03.controller.buiness;

import com.team03.payload.request.business.AdvertTypeRequest;
import com.team03.payload.response.business.AdvertTypeResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.AdvertTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advert-types")
@RequiredArgsConstructor
public class AdvertTypeController {
    public final AdvertTypeService advertTypeService;

    @PostMapping()
    @Operation(tags = "Advert Type", summary = "T03",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertTypeResponse> createAdvertType(@RequestBody @Valid AdvertTypeRequest advertTypeRequest) {
        return advertTypeService.saveAdvertType(advertTypeRequest) ;

    }

    @GetMapping()
    @Operation(tags = "Advert Type", summary = "T01")
    public ResponseMessage<List<AdvertTypeResponse>> getAdvertTypes(){
        return advertTypeService.getAdvertTypes();
    }

    @GetMapping("/{id}")
    @Operation(tags = "Advert Type", summary = "T02",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertTypeResponse> getAdvertTypesById(@PathVariable Long id){
        return advertTypeService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(tags = "Advert Type", summary = "T04",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertTypeResponse> updateAdvertTypeById(@PathVariable Long id,
                                                                    @RequestBody AdvertTypeRequest advertTypeRequest){
        return advertTypeService.updateAdvertTypeById(id,advertTypeRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(tags = "Advert Type", summary = "T05",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertTypeResponse> deleteAdvertTypeById(@PathVariable Long id){
        return advertTypeService.deleteAdvertTypeById(id);
    }


}
