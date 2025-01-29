package com.team03.contactmessage.entity;


import lombok.Getter;

@Getter
public enum Status {

    UNREAD ("Unread",0),
    READ("Read",1);

    public final int status;
    public final String name;

    Status(String name,int status) {
        this.name=name;
        this.status = status;
    }

}