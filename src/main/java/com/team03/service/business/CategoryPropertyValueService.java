package com.team03.service.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.CategoryPropertyKey;
import com.team03.entity.business.CategoryPropertyValue;
import com.team03.payload.mappers.CategoryPropertyValueMapper;
import com.team03.payload.request.business.PropertyRequest;
import com.team03.payload.response.business.PropertyResponse;
import com.team03.repository.business.CategoryPropertyValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryPropertyValueService {

    private final CategoryPropertyValueRepository valueRepository;
    private final CategoryPropertyKeyService categoryPropertyKeyService;
    private final CategoryPropertyValueMapper valueMapper;

    public PropertyResponse saveCategoryPropertyValue(PropertyRequest propertyDTO, Advert advert){
        CategoryPropertyKey key = categoryPropertyKeyService.getCategoryPropertyKeyById(propertyDTO.getKeyId());

        CategoryPropertyValue value = CategoryPropertyValue.builder()
                .value(propertyDTO.getValue())
                .advert(advert)
                .categoryPropertyKey(key)
                .build();
        CategoryPropertyValue savedPropertyValue = valueRepository.save(value);

        return valueMapper.categoryPropertyValueToPropertyResponse(key, savedPropertyValue);
    }
    public PropertyResponse updateCategoryPropertyValue(PropertyRequest propertyDTO, Advert advert){
        CategoryPropertyKey key = categoryPropertyKeyService.getCategoryPropertyKeyById(propertyDTO.getKeyId());
        Optional<CategoryPropertyValue> optionalValue =valueRepository.findByCategoryPropertyKeyIdAndAdvertId(key.getId(), advert.getId());
        if (optionalValue.isEmpty()){
            return saveCategoryPropertyValue(propertyDTO, advert);
        }
        CategoryPropertyValue value = optionalValue.get();
        value.setValue(propertyDTO.getValue());
        CategoryPropertyValue savedPropertyValue = valueRepository.save(value);
        return valueMapper.categoryPropertyValueToPropertyResponse(key, savedPropertyValue);
    }
}
