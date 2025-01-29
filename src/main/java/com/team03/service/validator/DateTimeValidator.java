package com.team03.service.validator;

import com.team03.entity.business.TourRequest;
import com.team03.exception.ConflictException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.request.business.TourRequestRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Component
public class DateTimeValidator {
    public void checkDateTime(List<TourRequest> existsTourRequests, TourRequestRequest newTourRequest){
        //kontrol iki asamali önce gün sonra saat kontrolu olmali
        // tarih kontrolu yap
        for ( TourRequest existstourRequest: existsTourRequests) {
            LocalDate day= existstourRequest.getTourDate();
            LocalTime time= existstourRequest.getTourTime();
            //önce günleri kontrol ediyoruz
            if (newTourRequest.getTour_date()==day){
                //günler ayni ise saatleri
                if (newTourRequest.getTour_time()==time){
                    throw new ConflictException(MessageUtil.getMessage("tour.request.has.datetime.conflict"));
                }
            }

        }

        //sadece onaylanmis olanlar icinde mi mantikli bekleyenleri ne yapmak gerek?
    }
}
