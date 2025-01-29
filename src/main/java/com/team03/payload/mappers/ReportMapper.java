package com.team03.payload.mappers;

import com.team03.entity.business.Advert;
import com.team03.entity.business.TourRequest;
import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;
import com.team03.payload.response.excelResponses.AdvertExcelResponse;
import com.team03.payload.response.excelResponses.TourRequestExcelResponse;
import com.team03.payload.response.excelResponses.UserExcelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReportMapper {


    public AdvertExcelResponse mapToAdvertExcelResponse(Advert advert) {
        return AdvertExcelResponse.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .advertStatus(advert.getStatus().getName())
                .builtIn(advert.getBuiltIn())
                .isActive(advert.getIsActive())
                .viewCount(advert.getViewCount())
                .advertType(advert.getAdvertType().getTitle())
                .countryName(advert.getCountry().getName())
                .cityName(advert.getCity().getName())
                .districtName(advert.getDistrict().getName())
                .userName(advert.getUser().getFirstName() + " " + advert.getUser().getLastName())
                .createdAt(advert.getCreateAt())
                .build();
    }



    public UserExcelResponse userToUserExcelResponse(User user) {
        Set<String> roles= user.getUserRoles().stream().map(UserRole::getName).collect(Collectors.toSet());
        return UserExcelResponse.builder()
                .id(user.getId())
                .name(user.getFirstName()+ " "+ user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .builtIn(user.getBuiltIn())
                .role(roles)
                .build();
    }


    public TourRequestExcelResponse toTourRequestExcelResource(TourRequest tourRequest) {
        return TourRequestExcelResponse.builder()
                .id(tourRequest.getId())
                .tourReqStatus(tourRequest.getTourReqStatus().getName())
                .createAt(tourRequest.getCreateAt())
                .guestUserName(tourRequest.getGuestUser().getFirstName() + " " +tourRequest.getGuestUser().getLastName())
                .ownerUserName(tourRequest.getOwnerUser().getFirstName()+" " + tourRequest.getOwnerUser().getLastName())
                .tourDate(tourRequest.getTourDate())
                .advertId(tourRequest.getAdvert().getId())
                .tourTime(tourRequest.getTourTime())
                .advertTitle(tourRequest.getAdvert().getTitle())
                .advertPrice(tourRequest.getAdvert().getPrice())
                .countryName(tourRequest.getAdvert().getCountry().getName())
                .cityName(tourRequest.getAdvert().getCity().getName())
                .districtName(tourRequest.getAdvert().getDistrict().getName())
                .advertSlug(tourRequest.getAdvert().getSlug())
                .build();
    }

}
