package com.team03.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryAmountResponse implements Serializable {

    private String category_title;
    private Long category_id;
    private String icon;
    private Long amount;

}
