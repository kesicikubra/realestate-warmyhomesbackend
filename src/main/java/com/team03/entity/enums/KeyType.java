package com.team03.entity.enums;

import lombok.Getter;

@Getter
public enum KeyType {
    BOOLEAN("Boolean"),
    NUMBER("Number"),
    TEXT("Text");
    public final String name;

    KeyType(String name){
        this.name=name;
    }

}
