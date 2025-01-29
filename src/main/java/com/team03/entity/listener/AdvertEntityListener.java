package com.team03.entity.listener;

import com.team03.entity.business.Advert;
import com.team03.entity.enums.AdvertStatus;
import com.team03.entity.enums.LogStatus;
import com.team03.service.business.LogService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AdvertEntityListener {

    private String desc;

    private final LogService logService;

    public AdvertEntityListener(@Lazy LogService logService) {
        this.logService = logService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AdvertEntityListener.class);
    @PostPersist
    public void postPersist(Advert advert) {
        desc="Advert is created and wait for approve";
        logger.info(advert.getId()+ " id "+desc);

        logService.createLog(LogStatus.CREATED.log, advert.getUser(), advert, advert.getCreateAt(), desc);


    }

    @PostUpdate
    public void postUpdate(Advert advert) {
        if (advert.getStatus()== AdvertStatus.ACTIVATED){
            desc="Advert is activated";
            logger.info(advert.getId()+ " id "+desc);
            logService.createLog(LogStatus.UPDATED.log, advert.getUser(), advert, advert.getUpdateAt(), desc);
        } else if (advert.getStatus()== AdvertStatus.REJECTED) {
            desc="Advert is declined by manager";
            logger.info(advert.getId()+ " id "+desc);
            logService.createLog(LogStatus.DECLINED.log, advert.getUser(), advert, advert.getUpdateAt(), desc);

        }
    }

    @PreRemove
    public void postRemove(Advert advert) {
        desc="Advert is deleted";
        logger.info(advert.getId() + " id "+desc);

        logService.createLog(LogStatus.DELETED.log, advert.getUser(), advert, advert.getCreateAt(), desc);

    }
}
