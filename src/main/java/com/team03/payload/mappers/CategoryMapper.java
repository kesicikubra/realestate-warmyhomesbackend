package com.team03.payload.mappers;

import com.team03.entity.business.Category;

import com.team03.i18n.MessageUtil;
import com.team03.payload.request.business.CategoryRequest;
import com.team03.payload.request.business.CategoryUpdateRequest;
import com.team03.payload.response.business.CategoryResponse;
import com.team03.payload.response.business.CategoryResponseWithoutPropertyKey;
import com.team03.payload.response.business.CategoryUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CategoryMapper {

    private final CategoryPropertyKeyMapper categoryPropertyKeyMapper;

    public Category requestToCategory(CategoryRequest categoryRequest){

        return Category.builder()
                .title(categoryRequest.getTitle())
                .icon(categoryRequest.getIcon())
                .seq(categoryRequest.getSeq())
                .isActive(categoryRequest.getIsActive())
                .builtIn(false)
                .createAt(LocalDateTime.now())
                .build();

    }
    public Category requestToUpdateCategory(CategoryUpdateRequest categoryUpdateRequest){

        return Category.builder()
                .title(categoryUpdateRequest.getTitle())
                .icon(categoryUpdateRequest.getIcon())
                .seq(categoryUpdateRequest.getSeq())
                .isActive(categoryUpdateRequest.getIsActive())
                .builtIn(false)
                .updateAt(LocalDateTime.now())
                .build();

    }
    public CategoryUpdateResponse updateCategoryToResponse(Category category){
        return CategoryUpdateResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .icon(category.getIcon())
                .slug(category.getSlug())
                .seq(category.getSeq())
                .isActive(category.getIsActive())
                .createAt(category.getCreateAt())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public CategoryResponse CategoryToResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .title(Objects.equals(category.getBuiltIn(), true) ?
                        MessageUtil.getMessage(category.getTitle()) : category.getTitle())
                .icon(category.getIcon())
                .slug(category.getSlug())
                .seq(category.getSeq())
                .isActive(category.getIsActive())
                .createAt(category.getCreateAt())
                .categoryPropertyKey(categoryPropertyKeyMapper.CPKToResponses(category.getCategoryPropertyKeys(), category.getId()))
                .build();
    }

    public CategoryResponseWithoutPropertyKey CategoryToWithoutPropertyKeyResponse(Category category){
        return CategoryResponseWithoutPropertyKey.builder()
                .id(category.getId())
                .title(category.getTitle())
                .icon(category.getIcon())
                .slug(category.getSlug())
                .seq(category.getSeq())
                .isActive(category.getIsActive())
                .createAt(category.getCreateAt())
                .build();
    }
}
