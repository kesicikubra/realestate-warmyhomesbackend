package com.team03.service.business;


import com.team03.entity.business.Category;
import com.team03.entity.business.CategoryPropertyKey;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.CategoryPropertyKeyMapper;
import com.team03.payload.request.business.CategoryPropertyKeyRequest;
import com.team03.payload.response.business.CategoryPropertyKeyResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.CategoryPropertyKeyRepository;
import com.team03.repository.business.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class CategoryPropertyKeyService {

    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final CategoryPropertyKeyMapper categoryPropertyKeyMapper;
    private final CategoryRepository categoryRepository;

    //post
    public ResponseMessage<CategoryPropertyKeyResponse> saveCategoryPropertyKeyWithRequest(Long id, CategoryPropertyKeyRequest categoryPropertyKeyRequest) {
        //bu id ile kategori yoksa hata
        Category category = getCategoryById(id);
        //bu category icinde bu isimde baska bir isim varsa hata firlat,baska kategoride varsa firlatma
        isExistsByNameInProperties(category.getCategoryPropertyKeys(), categoryPropertyKeyRequest.getName());

        CategoryPropertyKey categoryPropertyKey = categoryPropertyKeyMapper.requestToCPK(categoryPropertyKeyRequest);

        CategoryPropertyKey savedCPK = categoryPropertyKeyRepository.save(categoryPropertyKey);

        category.getCategoryPropertyKeys().add(savedCPK);
        categoryRepository.save(category);

        CategoryPropertyKeyResponse response = categoryPropertyKeyMapper.CPKToResponse(savedCPK);
        response.setCategoryId(id);

        return ResponseMessage.<CategoryPropertyKeyResponse>builder()
                .message(MessageUtil.getMessage("category.property.key.create"))
                .httpStatus(HttpStatus.CREATED)
                .object(response)
                .build();
    }


    // helper getCategoryPropertyKeyById
    public CategoryPropertyKey getCategoryPropertyKeyById(Long keyId) {
        return categoryPropertyKeyRepository.findById(keyId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.category.property.key",keyId)));

    }


    // helper getCategoryById
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.by.id",categoryId)));
    }

    //helper uniq control
    public void isExistsByNameInProperties(Set<CategoryPropertyKey> categoryPropertyKeySet,String name) {
        for (CategoryPropertyKey cpk : categoryPropertyKeySet) {
            if (cpk.getName().equals(name)) {
                throw new BadRequestException(MessageUtil.getMessage("already.exist",name));
            }
        }
    }

    //built in control
    public void isThisBuiltIn(Boolean built_in) {
        if (built_in) {
            throw new BadRequestException(MessageUtil.getMessage("built.in.value.error"));
        }
    }


    //update category property key
    public ResponseMessage<CategoryPropertyKeyResponse> updateCategoryPropertyKey(Long id,
                                                                                  CategoryPropertyKeyRequest CPKRequest) {
        CategoryPropertyKey categoryPropertyKey = getCategoryPropertyKeyById(id); // repoda var mi?

        isThisBuiltIn(categoryPropertyKey.getBuiltIn()); // built in ise hata ?

        Category category=categoryPropertyKeyRepository.findByCategoryPropertyKeyId(id);
        isExistsByNameInProperties(category.getCategoryPropertyKeys(), CPKRequest.getName()); //uniq control




        CategoryPropertyKey cpk = categoryPropertyKeyMapper.updateRequestToCPK(CPKRequest, id);// request ile gelen entitye cevrildi
        CategoryPropertyKey savedCPK = categoryPropertyKeyRepository.save(cpk); // id cakildi
        CategoryPropertyKeyResponse response = categoryPropertyKeyMapper.CPKToResponse(savedCPK);//responsa cevrildi
        response.setCategoryId(category.getId());


        return ResponseMessage.<CategoryPropertyKeyResponse>builder()
                .message(MessageUtil.getMessage("category.property.key.update"))
                .httpStatus(HttpStatus.OK)
                .object(response)
                .build();

    }

    //delete CPK
    public ResponseMessage<CategoryPropertyKeyResponse> deleteCategoryPropertyKeyById(Long id) {

        Category category=categoryPropertyKeyRepository.findByCategoryPropertyKeyId(id);
        CategoryPropertyKey categoryPropertyKey = getCategoryPropertyKeyById(id); // repoda var mi?
        isThisBuiltIn(categoryPropertyKey.getBuiltIn()); //built in kontrolu ?
        categoryPropertyKeyRepository.deleteById(id);

        CategoryPropertyKeyResponse response = categoryPropertyKeyMapper.CPKToResponse(categoryPropertyKey);//responsa cevrildi
        response.setCategoryId(category.getId());


        return ResponseMessage.<CategoryPropertyKeyResponse>builder()
                .message(MessageUtil.getMessage("category.property.key.delete"))
                .httpStatus(HttpStatus.OK)
                .object(response)
                .build();
    }
}
