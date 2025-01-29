package com.team03.payload.response.business;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertTypeResponse implements Serializable {

    private Long id;

    private String title;


}
