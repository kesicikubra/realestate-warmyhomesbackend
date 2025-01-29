package com.team03.payload.response.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PhotoResponse {

    private Long id;
    private byte[] data;
    private String name;
    private String type;
}
