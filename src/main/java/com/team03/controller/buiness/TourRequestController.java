package com.team03.controller.buiness;

import com.team03.payload.request.business.TourRequestRequest;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.business.TourRequestResponse;
import com.team03.service.business.TourRequestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tour-requests")
@RequiredArgsConstructor
public class TourRequestController {

    private final TourRequestService tourRequestService;

    //S01
    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S01",description = "CUSTOMER")
    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedUsersAllTourRequest(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type,
            HttpServletRequest httpServletRequest){

        return tourRequestService.getAuthenticatedUsersAllTourRequest(q,page,size,sort,type,httpServletRequest);

    }
    //S02
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(tags = "Tour Request",summary = "S02",description = "ADMIN MANAGER")
    public ResponseMessage<Page<TourRequestResponse>> getALlTourRequestForManagerAndAdmin(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){

        return tourRequestService.getAllTourRequestForManager(q,page,size,sort,type);

    }

    //S03
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S03",description = "CUSTOMER")
    public ResponseMessage <TourRequestResponse> getTourRequestByAuthenticatedUser(@PathVariable Long id,
                                                                                   HttpServletRequest httpServletRequest){

        return tourRequestService.getTourRequestByAuthenticatedUser(id,httpServletRequest);

    }

    //S04
    @GetMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(tags = "Tour Request",summary = "S04",description = "ADMIN MANAGER")
    public ResponseMessage <TourRequestResponse> getTourRequestForAdminandManager(@PathVariable Long id){
        return tourRequestService.getTourRequestForAdminandManager(id);
    }



    //S05

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S05",description = "CUSTOMER")
    public ResponseMessage <TourRequestResponse> createTourRequest(@Valid @RequestBody TourRequestRequest tourRequestDto,
                                                                   HttpServletRequest httpServletRequest){

      return tourRequestService.createTourRequest(tourRequestDto, httpServletRequest);
    }
    //S06

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S06",description = "CUSTOMER")
    public ResponseMessage<TourRequestResponse> updateTourRequest (@PathVariable Long id,
                                                                   @Valid @RequestBody TourRequestRequest tourRequestDto,
                                                                   HttpServletRequest httpServletRequest){
        return tourRequestService.updateTourRequest(id,tourRequestDto,httpServletRequest);
    }

    //S07

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S07",description = "CUSTOMER")
    public ResponseMessage<TourRequestResponse> cancelTourRequest (@PathVariable Long id,
                                                                   HttpServletRequest httpServletRequest){
        return tourRequestService.cancelTourRequest(id,httpServletRequest);
    }

    //S08

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S08",description = "CUSTOMER")
    public ResponseMessage<TourRequestResponse> approveTourRequest (@PathVariable Long id,
                                                                    HttpServletRequest httpServletRequest){
        return tourRequestService.approveTourRequest(id,httpServletRequest);
    }

    //S09

    @PatchMapping("/{id}/decline")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S09",description = "CUSTOMER")
    public ResponseMessage<TourRequestResponse> declineTourRequest (@PathVariable Long id,
                                                                   HttpServletRequest httpServletRequest){
        return tourRequestService.declineTourRequest(id,httpServletRequest);
    }

    //S10


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Tour Request",summary = "S10",description = "ADMIN MANAGER")
    public ResponseMessage<TourRequestResponse> deleteTourRequest (@PathVariable Long id){
        return tourRequestService.deleteTourRequest(id);
    }

    @GetMapping("/auth-guest")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S01-guest",description = "CUSTOMER")
    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedGuestUsersAllTourRequest(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type,
            HttpServletRequest httpServletRequest){

        return tourRequestService.getAuthenticatedUsersAllGuestTourRequest(q,page,size,sort,type,httpServletRequest);

    }

    @GetMapping("/auth-owner")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Tour Request",summary = "S01-owner",description = "CUSTOMER")
    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedOwnerUsersAllTourRequest(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type,
            HttpServletRequest httpServletRequest){

        return tourRequestService.getAuthenticatedUsersAllOwnerTourRequest(q,page,size,sort,type,httpServletRequest);

    }







}
