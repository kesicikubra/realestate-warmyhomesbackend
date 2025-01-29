package com.team03.entity.enums;

import lombok.Getter;

@Getter
public enum AdvertStatus {

    PENDING("Pending",0), ACTIVATED ("Activated",1), REJECTED("Rejected",2);

    public final String name;
    public final int id;
    AdvertStatus (String name, int id){
        this.name = name;
        this.id =id;
    }

    public Integer getId() {
        return id;
    }
}
