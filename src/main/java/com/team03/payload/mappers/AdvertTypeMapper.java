package com.team03.payload.mappers;

import com.team03.entity.business.AdvertType;
import com.team03.i18n.MessageUtil;
import com.team03.payload.request.business.AdvertTypeRequest;
import com.team03.payload.response.business.AdvertTypeResponse;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AdvertTypeMapper {

    public AdvertType requestToAdvertType(AdvertTypeRequest advertTypeRequest){
        return AdvertType.builder()
                .title(advertTypeRequest.getTitle())
                .builtIn(false)
                .build();
    }

    public AdvertTypeResponse responseToAdvertType(AdvertType advertType){
        AdvertTypeResponse advertTypeResponse= AdvertTypeResponse.builder()
                .id(advertType.getId())
                .title(Objects.equals(advertType.getBuiltIn(), true) ?
                        MessageUtil.getMessage(advertType.getTitle()) : advertType.getTitle())
                .build();

        System.out.println("AdvertType title: " + advertTypeResponse.getTitle());

        return advertTypeResponse;
    }
}
