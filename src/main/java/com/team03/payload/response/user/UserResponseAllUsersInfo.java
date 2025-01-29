package com.team03.payload.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.team03.payload.response.abstracts.BaseUserResponse;
import com.team03.payload.response.business.*;

import com.team03.service.helper.RestPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserResponseAllUsersInfo extends BaseUserResponse implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updateAt;

    private Page< AdvertResponseForGetUser > adverts;
    private RestPage< TourRequestResponse > tourRequests;
    private Page< LogResponse > logs;
    private Page< FavoriteResponse > favorites;

}
