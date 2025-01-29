package com.team03.payload.response.business;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImageResponse implements Serializable {

    private Long id;
    private byte[] imageData;
    private String name;
    private String type;
    private boolean featured;
    private boolean suitable;

}
