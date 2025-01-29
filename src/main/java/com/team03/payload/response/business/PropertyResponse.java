package com.team03.payload.response.business;

import com.team03.entity.enums.KeyType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PropertyResponse implements Serializable {

    private Long property_key_id;
    private KeyType key_type;
    private String key;
    private String value;
    private String unit;
}
