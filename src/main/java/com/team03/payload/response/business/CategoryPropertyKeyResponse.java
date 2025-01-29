package com.team03.payload.response.business;

import com.team03.entity.enums.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryPropertyKeyResponse implements Serializable {

    private Long id;
    private Long categoryId;

    private String name;

    private KeyType keyType;

    private String unit;

}
