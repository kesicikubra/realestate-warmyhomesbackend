package com.team03.controller.buiness;

import com.team03.payload.response.business.AdvertResponse;
import com.team03.payload.response.business.FavoriteResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.FavoriteService;
import com.team03.service.helper.RestPage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;


    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(tags = "Favorites" ,summary = "K01",description ="CUSTOMER" )
    public ResponseMessage< Page<FavoriteResponse> > getAllFavorites(HttpServletRequest request,

                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "20") int size,
                                                                     @RequestParam(defaultValue = "createAt") String sort,
                                                                     @RequestParam(defaultValue = "desc") String type) {

        return favoriteService.getAllFavorites(request,page,size,sort,type);
    }


    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Favorites" ,summary = "K02",description ="ADMIN MANAGER")
    public ResponseMessage< RestPage< FavoriteResponse > >getUserFavorites(@PathVariable(name = "id") Long id,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "20") int size,
                                                                           @RequestParam(defaultValue = "createAt") String sort,
                                                                           @RequestParam(defaultValue = "desc") String type                                                       ){
        return favoriteService.getUserFavorites(id,page,size,sort,type);
    }


    @PostMapping("/{id}/auth")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(tags = "Favorites" ,summary = "K03",description ="CUSTOMER")
    public ResponseMessage<AdvertResponse>addOrRemoveFavorite(@PathVariable(name = "id") Long advertId, HttpServletRequest request){
        return favoriteService.addOrRemoveFavorite(advertId, request);
    }


    @DeleteMapping("/auth")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(tags = "Favorites" ,summary = "K04",description ="CUSTOMER")
    public ResponseMessage<String>deleteAllFavorite(HttpServletRequest request){
        return favoriteService.deleteAllFavorite(request);
    }


    @DeleteMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Favorites" ,summary = "K05",description ="ADMIN MANAGER")
    public ResponseMessage<String> removeFavoritesByUserId(@RequestParam(name = "user_id") Long userId){
        return favoriteService.removeFavoritesByUserId(userId);
    }


    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Favorites" ,summary = "K06",description ="ADMIN MANAGER")
    public ResponseMessage<String >removeFavoriteById(@PathVariable(name = "id") Long favoriteId){
        return  favoriteService.removeFavoriteById(favoriteId);
    }

}
