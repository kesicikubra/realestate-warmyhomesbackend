package com.team03.payload.mappers;

import com.team03.entity.business.Favorite;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.FavoriteResponse;
import com.team03.service.business.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {


    private final ImageService imageService;

    public FavoriteResponse toFavoriteResponse(Favorite favorite){
        return FavoriteResponse.builder ()
                .id(favorite.getAdvert ().getId ())
                .category_title (Objects.equals(favorite.getAdvert().getCategory().getBuiltIn(), true) ?
                        MessageUtil.getMessage(favorite.getAdvert().getCategory().getTitle()) : favorite.getAdvert ().getCategory().getTitle())
                .title(favorite.getAdvert ().getTitle ())
                .price(favorite.getAdvert ().getPrice())
                .location(favorite.getAdvert ().getLocation())
                .featuredImage(imageService.findFeaturedImageByAdvertId(favorite.getAdvert ().getId()))
                .advert_type(Objects.equals(favorite.getAdvert().getAdvertType().getBuiltIn(), true) ?
                        MessageUtil.getMessage(favorite.getAdvert().getAdvertType().getTitle()) :  favorite.getAdvert().getAdvertType().getTitle())
                .country(favorite.getAdvert ().getCountry().getName())
                .city(favorite.getAdvert ().getCity().getName())
                .district(favorite.getAdvert ().getDistrict().getName())
                .category_id (favorite.getAdvert ().getCategory ().getId ())
                .createdAt (favorite.getCreateAt ())
                .build();
    }
}
