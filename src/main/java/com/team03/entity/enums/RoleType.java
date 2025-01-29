package com.team03.entity.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    MANAGER("Manager"),
    CUSTOMER("Customer"),
    ADMIN("Admin");

    public final String name;
    RoleType (String name){
        this.name = name;
    }
}
