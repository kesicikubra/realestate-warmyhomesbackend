package com.team03.controller.buiness;

import com.team03.payload.request.business.AdvertRequest;
import com.team03.payload.request.business.AdvertRequestUpdateAdmin;
import com.team03.payload.request.business.AdvertRequestWithoutImages;
import com.team03.payload.response.business.*;
import com.team03.service.business.AdvertService;
import com.team03.service.helper.RestPage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adverts")
public class AdvertController {

    private final AdvertService advertService;

    // A10
    @PostMapping
    @Operation(tags = "Advert", summary = "A10",description = "CUSTOMER")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertWithPropertiesResponse> saveAdvert(@Valid @ModelAttribute AdvertRequest advertRequest, HttpServletRequest httpRequest){

        return advertService.saveAdvert(advertRequest, httpRequest);
    }

    // A02
    @GetMapping("/cities")
    @Operation(tags = "Advert", summary = "A02")
    public ResponseMessage<List<CityAmountResponse>> getCitiesAmountByAdvert(){
        return advertService.getCitiesAmountByAdvert();
    }

    // A03
    @GetMapping("/categories")
    @Operation(tags = "Advert", summary = "A03")
    public ResponseMessage<List<CategoryAmountResponse>> getCategoriesAmountByAdvert(){
        return advertService.getCategoriesAmountByAdvert();
    }

    // A05 /adverts/auth?page=1&size=10&sort=date&type=asc
    @GetMapping("/auth")
    @Operation(tags = "Advert", summary = "A05",description = "CUSTOMER")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<Page<AdvertResponseWithImageAndTourReq>> getAuthenticatedUserAdverts(
            HttpServletRequest request,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "category") String sort,
            @RequestParam(value = "type",defaultValue = "asc") String type){
        return advertService.getAuthenticatedUserAdverts(request, page, size, sort, type);
    }

    // A08 -------> /adverts/23/auth
    @GetMapping("/{id}/auth")
    @Operation(tags = "Advert", summary = "A08",description = "CUSTOMER")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponseWithTourRequest> getAuthenticatedUserAdvertById(HttpServletRequest request, @PathVariable Long id){
        return advertService.getAuthenticatedUserAdvertById(request, id);
    }

    // A07 ----->  /adverts/lux-villa-in-river-park
    @GetMapping("/{slug}")
    @Operation(tags = "Advert", summary = "A07")
    public ResponseMessage<AdvertResponseWithTourRequest> getAdvertBySlug(@PathVariable String slug, HttpServletRequest request){
        return advertService.getAdvertBySlug(slug, request);
    }

    // A01  ------>  /adverts?q=beyoglu&category_id=12&advert_type_id=3&price_start=500&price_end=1500&status=1;page=1&size =10&sort=date&type=asc
    @GetMapping
    @Operation(tags = "Advert", summary = "A01")
    public ResponseMessage< RestPage<AdvertResponseWithFeaturedImg> > searchAdverts(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "advert_type_id", required = false) Long advertTypeId,
            @RequestParam(name = "price_start", required = false) Double priceStart,
            @RequestParam(name = "price_end", required = false) Double priceEnd,
            @RequestParam(name = "city_id", required = false) Long cityId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = "category") String sort,
            @RequestParam(name = "type", defaultValue = "asc") String type,
            HttpServletRequest request) {

        return advertService.searchAdverts(query, categoryId, advertTypeId, priceStart, priceEnd, page, size, sort, type, cityId, request);
    }

    // A06   /adverts/admin/q=beyoğlu&category_id=12&advert_type_id=3&price_start=500&price_end=1500&status=1;page =1&size=10&sort=date&type=asc
    @GetMapping("/admin")
    @Operation(tags = "Advert", summary = "A06",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<Page<AdvertResponseWithImageAndTourReq>> searchAdvertsByAdminAndManager(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "advert_type_id", required = false) Long advertTypeId,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = "category") String sort,
            @RequestParam(name = "type", defaultValue = "asc") String type) {

        return advertService.searchAdvertsByAdminAndManager(
                query, categoryId, advertTypeId, status, page, size, sort, type);
    }

    // A09  -------->  /adverts/23/admin
    @GetMapping("/{id}/admin")
    @Operation(tags = "Advert", summary = "A09",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<AdvertResponseWithBuiltIn> getAdvertById(@PathVariable Long id){
        return advertService.getAdvertById(id);
    }


    //  A11 ------->  /adverts/auth/23
    @PutMapping("/auth/{id}")
    @Operation(tags = "Advert", summary = "A11",description = "CUSTOMER")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponseWithoutImage> updateAdvertByCustomer(HttpServletRequest request, @PathVariable Long id,
                                                                              @RequestBody AdvertRequestWithoutImages advertRequest){
        return advertService.updateAdvertByCustomer(request, id, advertRequest);
    }

    // A12   ----------> /adverts/admin/23
    @PutMapping("/admin/{id}")
    @Operation(tags = "Advert", summary = "A12",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<AdvertResponseWithoutImage> updateAdvertByAdmin(@PathVariable Long id, @RequestBody AdvertRequestUpdateAdmin advertRequest){
        return advertService.updateAdvert(id, advertRequest);
    }

    // A13 ----------> /adverts/admin/23
    @DeleteMapping("/admin/{id}")
    @Operation(tags = "Advert", summary = "A13",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<AdvertResponseWithoutImage> deleteAdvert(@PathVariable Long id){
        return advertService.deleteAdvert(id);
    }

    // A04  ---> /adverts/popular/20
    @GetMapping("/popular")
    @Operation(tags = "Advert", summary = "A04")
    public ResponseMessage<List<AdvertResponseWithFeaturedImg>> getPopularAdverts(@RequestParam(value = "amount", defaultValue = "10") int amount, HttpServletRequest request){
        return advertService.getPopularAdverts(amount, request);
    }

}
