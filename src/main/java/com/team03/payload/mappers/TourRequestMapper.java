package com.team03.payload.mappers;

import com.team03.entity.business.Advert;
import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.TourReqStatus;
import com.team03.entity.user.User;
import com.team03.payload.request.business.TourRequestRequest;
import com.team03.payload.response.business.TourRequestResponse;
import com.team03.payload.response.business.TourRequestResponseForAdvert;
import com.team03.service.business.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TourRequestMapper {

    private final ImageService imageService;

    public TourRequest requestDtoToTourRequest(TourRequestRequest tourRequestRequest, Advert advert){

        return TourRequest.builder()
                .advert(advert)
                .tourTime(tourRequestRequest.getTour_time())
                .tourDate(tourRequestRequest.getTour_date())
                .build();


    }


    public TourRequestResponse tourRequestToResponse(TourRequest tourRequest){


        return TourRequestResponse.builder()
                .id(tourRequest.getId())
                .tourReqStatus(tourRequest.getTourReqStatus())
                .createAt(tourRequest.getCreateAt())
                .guestUserName(tourRequest.getGuestUser().getFirstName() + " " +tourRequest.getGuestUser().getLastName())
                .ownerUserName(tourRequest.getOwnerUser().getFirstName()+" " + tourRequest.getOwnerUser().getLastName())
                .tourDate(tourRequest.getTourDate())
                .tourTime(tourRequest.getTourTime())
                .advertTitle(tourRequest.getAdvert().getTitle())
                .advertPrice(tourRequest.getAdvert().getPrice())
                .countryName(tourRequest.getAdvert().getCountry().getName())
                .cityName(tourRequest.getAdvert().getCity().getName())
                .districtName(tourRequest.getAdvert().getDistrict().getName())
                .advertSlug(tourRequest.getAdvert().getSlug())
                .imageData(imageService.findFeaturedImageByAdvertId(tourRequest.getAdvert().getId()))
                .build();
    }

    public TourRequestResponseForAdvert tourRequestToResponseForAdvert(TourRequest tourRequest){

        return TourRequestResponseForAdvert.builder()
                .id(tourRequest.getId())
                .tourReqStatus(tourRequest.getTourReqStatus())
                .createAt(tourRequest.getCreateAt())
                .guestUserName(tourRequest.getGuestUser().getFirstName() + " " +tourRequest.getGuestUser().getLastName())
                .ownerUserName(tourRequest.getOwnerUser().getFirstName()+" " + tourRequest.getOwnerUser().getLastName())
                .tourDate(tourRequest.getTourDate())
                .tourTime(tourRequest.getTourTime())
                .updateAt(tourRequest.getUpdateAt())
                .build();
    }

    public TourRequest TourReqRequestroUpdatedTourRequest(Long id, Advert advert, TourRequestRequest tourRequestRequest, LocalDateTime createTime, User guestUser){
        return TourRequest.builder()
                .id(id)
                .advert(advert)
                .tourTime(tourRequestRequest.getTour_time())
                .tourDate(tourRequestRequest.getTour_date())
                .createAt(createTime)
                .tourReqStatus(TourReqStatus.PENDING)
                .ownerUser(advert.getUser())
                .guestUser(guestUser)
                .build();


    }


}
