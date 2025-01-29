package com.team03.entity.listener;

import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.LogStatus;
import com.team03.entity.enums.TourReqStatus;
import com.team03.service.business.LogService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
@Component
public class TourRequestEntityListener {
    private String desc;
    private static final Logger logger = LoggerFactory.getLogger(TourRequestEntityListener.class);

    private final LogService logService;

    public TourRequestEntityListener(@Lazy LogService logService) {
        this.logService = logService;
    }

    @PostPersist
    public void postPersist(TourRequest tourRequest) {
        desc="Tour request is created";
        logger.info( tourRequest.getId() + " id "+ desc);
        logService.createLog(LogStatus.CREATED.log, tourRequest.getGuestUser(), tourRequest.getAdvert(), tourRequest.getCreateAt(), desc);

    }

    @PostUpdate
    public void postUpdate(TourRequest tourRequest) {

        if (tourRequest.getTourReqStatus()== TourReqStatus.APPROVED){
            desc="Tour request is accepted";
            logger.info( tourRequest.getId()+ " id "+ desc);
            logService.createLog(LogStatus.ACCEPTED.log, tourRequest.getGuestUser(), tourRequest.getAdvert(), tourRequest.getUpdateAt(), desc);
        }else if (tourRequest.getTourReqStatus()== TourReqStatus.DECLINED) {
            desc="Tour request is declined";
            logger.info(tourRequest.getId()+ " id "+desc);
            logService.createLog(LogStatus.DECLINED.log, tourRequest.getGuestUser(), tourRequest.getAdvert(), tourRequest.getUpdateAt(), desc);
        } else if (tourRequest.getTourReqStatus()==TourReqStatus.CANCELED) {
            desc="Tour request is canceled";
            logger.info(tourRequest.getId()+ " id "+ desc);
            logService.createLog(LogStatus.CANCELED.log, tourRequest.getGuestUser(), tourRequest.getAdvert(), tourRequest.getUpdateAt(), desc);
        }
    }

    @PreRemove
    public void postRemove(TourRequest tourRequest) {
        desc="Tour request is deleted";
        logger.info(tourRequest.getId()+ " id "+desc);
        logService.createLog(LogStatus.DELETED.log, tourRequest.getGuestUser(), tourRequest.getAdvert(), tourRequest.getCreateAt(), desc);
    }

}

