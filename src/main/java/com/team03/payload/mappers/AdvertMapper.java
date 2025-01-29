package com.team03.payload.mappers;

import com.team03.entity.business.*;
import com.team03.entity.user.User;
import com.team03.payload.request.abstracts.BaseAdvertRequest;
import com.team03.payload.response.business.*;
import com.team03.payload.response.user.UserResponseForAdvert;
import com.team03.service.business.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvertMapper {

    private final ImageService imageService;
    private final TourRequestMapper tourRequestMapper;
    private final AdvertTypeMapper advertTypeMapper;

    public AdvertWithPropertiesResponse advertToAdvertResponseWithProperties(Advert advert, List<PropertyResponse> properties){

        return AdvertWithPropertiesResponse.builder()

                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .images(imageService.findImagesByAdvertId(advert.getId()))
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .properties(properties)
                .view_count(advert.getViewCount())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .address(advert.getAddress())
                .build();
    }
    public AdvertResponseWithoutImage advertToAdvertResponseWithoutImage(Advert advert, List<PropertyResponse> properties){

        return AdvertResponseWithoutImage.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .properties(properties)
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .view_count(advert.getViewCount())
                .address(advert.getAddress())
                .build();
    }

    public AdvertResponse advertToAdvertResponse(Advert advert){

        return AdvertResponse.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .images( imageService.findImagesByAdvertId(advert.getId()))
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .view_count(advert.getViewCount())
                .address(advert.getAddress())
                .build();
    }

    public AdvertResponseWithFeaturedImg advertToAdvertResponseWithFeaturedImg(Advert advert, boolean favorited){

        return AdvertResponseWithFeaturedImg.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .featured_image(imageService.findFeaturedImageByAdvertId(advert.getId()))
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .view_count(advert.getViewCount())
                .address(advert.getAddress())
                .favorited_advert(favorited)
                .build();
    }

    public AdvertResponseWithImageAndTourReq advertToAdvertResponseWithImageAndTourReq(Advert advert){

        return AdvertResponseWithImageAndTourReq.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .featured_image(imageService.findFeaturedImageByAdvertId(advert.getId()))
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .view_count(advert.getViewCount())
                .tour_request_number(advert.getTourRequests().size())
                .favorite_number(advert.getFavorites().size())
                .address(advert.getAddress())
                .build();
    }

    public AdvertResponseForGetUser advertToAdvertResponseForGetUser(Advert advert){
        return AdvertResponseForGetUser.builder ()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .featured_image(imageService.findFeaturedImageByAdvertId(advert.getId()))
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .view_count(advert.getViewCount())
                .tour_request_number(advert.getTourRequests().size())
                .favorite_number(advert.getFavorites().size())
                .address(advert.getAddress())
                .build ();
    }

    public Advert advertRequestToAdvert(BaseAdvertRequest advertRequest){

        return Advert.builder()
                .description(advertRequest.getDescription())
                .price(advertRequest.getPrice())
                .title(advertRequest.getTitle())
                .createAt(LocalDateTime.now())
                .location(advertRequest.getLocation())
                .address(advertRequest.getAddress())
                .build();
    }

    public Advert advertRequestToAdvertForUpdate(BaseAdvertRequest advertRequest, Advert advert){
        return advertRequestToAdvert(advertRequest)
                .toBuilder()
                .createAt(advert.getCreateAt())
                .images(advert.getImages())
                .builtIn(advert.getBuiltIn())
                .location (advert.getLocation ())
                .id(advert.getId())
                .viewCount(advert.getViewCount())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public AdvertResponseWithTourRequest advertToAdvertResponseWithTourRequest(Advert advert, List<PropertyResponse> properties){

        return AdvertResponseWithTourRequest.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .city(advert.getCity().getName())
                .district(advert.getDistrict().getName())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .images(imageService.findImagesByAdvertId(advert.getId()))
                .view_count(advert.getViewCount())
                .address(advert.getAddress())
                .properties(properties)
                .tourRequest(advert.getTourRequests().stream().map(tourRequestMapper::tourRequestToResponseForAdvert).toList())
                .country_id (advert.getCountry ().getId ())
                .city_id (advert.getCity ().getId ())
                .district_id (advert.getDistrict ().getId ())
                .category_id(advert.getCategory().getId())
                .category_title(advert.getCategory().getTitle())
                .owner_user_id(advert.getUser().getId())
                .build();
    }

    public AdvertResponseWithBuiltIn advertToAdvertResponseWithBuiltIn(Advert advert, List<PropertyResponse> properties){

        return AdvertResponseWithBuiltIn.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .status(advert.getStatus())
                .location(advert.getLocation())
                .advert_type(advertTypeMapper.responseToAdvertType(advert.getAdvertType()))
                .country(advert.getCountry().getName())
                .country_id (advert.getCountry ().getId ())
                .city(advert.getCity().getName())
                .city_id (advert.getCity ().getId ())
                .district(advert.getDistrict().getName())
                .district_id (advert.getDistrict ().getId ())
                .address(advert.getAddress())
                .create_at(advert.getCreateAt())
                .update_at(advert.getUpdateAt())
                .is_active(advert.getIsActive())
                .images(imageService.findImagesByAdvertId(advert.getId()))
                .built_in(advert.getBuiltIn())
                .view_count(advert.getViewCount())
                .tourRequest(advert.getTourRequests().stream().map(tourRequestMapper::tourRequestToResponseForAdvert).toList())
                .properties(properties)
                .category_id(advert.getCategory().getId())
                .category_title(advert.getCategory().getTitle())
                .user(this.mapUserToUserResponseForAdvert(advert.getUser()))
                .tour_request_number(advert.getTourRequests().size())
                .favorite_number(advert.getFavorites().size())
                .build();
    }

    // SORRR   ?????
    private UserResponseForAdvert mapUserToUserResponseForAdvert(User user) {
        return UserResponseForAdvert.builder()
                .id(user.getId())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .phone_number(user.getPhoneNumber())
                .create_at(user.getCreateAt())
                .update_at(user.getUpdateAt())
                .built_in(user.getBuiltIn())
                .build();
    }

}
