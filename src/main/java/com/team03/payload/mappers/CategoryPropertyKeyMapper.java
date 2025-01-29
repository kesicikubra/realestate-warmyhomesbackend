package com.team03.payload.mappers;

import com.team03.entity.business.CategoryPropertyKey;
import com.team03.i18n.MessageUtil;
import com.team03.payload.request.business.CategoryPropertyKeyRequest;
import com.team03.payload.response.business.CategoryPropertyKeyResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Component
public class CategoryPropertyKeyMapper {

    public CategoryPropertyKey requestToCPK(CategoryPropertyKeyRequest categoryPropertyKeyRequest){
        return CategoryPropertyKey.builder()
                .name(categoryPropertyKeyRequest.getName())
                .keyType(categoryPropertyKeyRequest.getKeyType())
                .unit(categoryPropertyKeyRequest.getUnit())
                .builtIn(false)
                .build();
    }

    public CategoryPropertyKeyResponse CPKToResponse(CategoryPropertyKey CPK){
        return CategoryPropertyKeyResponse.builder()
                .id(CPK.getId())
                .categoryId (CPK.getId ())
                .name(Objects.equals(CPK.getBuiltIn(), true) ?
                        MessageUtil.getMessage(CPK.getName()) : CPK.getName())
                .keyType(CPK.getKeyType())
                .unit(CPK.getUnit())
                .build();
    }



    public CategoryPropertyKey updateRequestToCPK(CategoryPropertyKeyRequest categoryPropertyKeyRequest,Long cpkId){
        return CategoryPropertyKey.builder()
                .id(cpkId)
                .name(categoryPropertyKeyRequest.getName())
                .keyType(categoryPropertyKeyRequest.getKeyType())
                .unit(categoryPropertyKeyRequest.getUnit())
                .builtIn(false)
                .build();
    }

    public Set<CategoryPropertyKeyResponse> CPKToResponses(Set<CategoryPropertyKey> CPKs, Long categoryId){
        Set<CategoryPropertyKeyResponse> responses = new HashSet<>();

        for (CategoryPropertyKey CPK : CPKs) {
            CategoryPropertyKeyResponse response = CategoryPropertyKeyResponse.builder()
                    .id(CPK.getId())
                    .name(Objects.equals(CPK.getBuiltIn(), true) ?
                            MessageUtil.getMessage(CPK.getName()) : CPK.getName())
                    .keyType(CPK.getKeyType())
                    .unit(CPK.getUnit())
                    .categoryId(categoryId)
                    .build();
            responses.add(response);
        }

        return responses;
    }




}
