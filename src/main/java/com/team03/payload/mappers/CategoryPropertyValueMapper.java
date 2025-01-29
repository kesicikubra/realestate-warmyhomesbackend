package com.team03.payload.mappers;

import com.team03.entity.business.CategoryPropertyKey;
import com.team03.entity.business.CategoryPropertyValue;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.PropertyResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryPropertyValueMapper {

    public PropertyResponse categoryPropertyValueToPropertyResponse(CategoryPropertyKey key,CategoryPropertyValue value){
        return PropertyResponse.builder()
                .key(key.getBuiltIn() ? MessageUtil.getMessage(key.getName()) : key.getName())
                .value(value.getValue())
                .property_key_id(key.getId())
                .key_type(key.getKeyType())
                .unit(value.getCategoryPropertyKey().getUnit())
                .build();
    }
}
