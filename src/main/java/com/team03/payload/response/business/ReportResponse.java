package com.team03.payload.response.business;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReportResponse implements Serializable {

    private Long userAmount;
    private Long advertAmount;
    private Long categoryAmount;
    private Long advertTypeAmount;
    private Long tourRequestAmount;

}
