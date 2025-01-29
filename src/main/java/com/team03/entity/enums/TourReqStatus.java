package com.team03.entity.enums;

import lombok.Getter;

@Getter
public enum TourReqStatus {
    PENDING("Pending",0),
    APPROVED("Approved",1),
    DECLINED("Declined",2),
    CANCELED("Canceled",3);

    public final String name;
    public final Integer id;
    TourReqStatus (String name, Integer id){
        this.name = name;
        this.id =id;
    }


}
