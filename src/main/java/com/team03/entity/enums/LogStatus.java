package com.team03.entity.enums;

public enum LogStatus {

    CREATED("CREATED"),

    UPDATED("UPDATED"),

    DELETED("DELETED"),

    ACCEPTED("ACCEPTED"),

    DECLINED("DECLINED"),

    CANCELED("CANCELED");

    public final String log;

    LogStatus(String log) {
        this.log = log;
    }
}
