package com.team03.payload.mappers;

import com.team03.entity.business.Log;
import com.team03.payload.response.business.LogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogMappers {

    public LogResponse logToLogResponse(Log log){

        return LogResponse.builder()
                .id(log.getId())
                .createAt(log.getCreateAt())
                .userId(log.getUser().getId())
                .description(log.getDescription())
                .advertId(log.getAdvert().getId())
                .log(log.getLog())
                .build();
    }

}
