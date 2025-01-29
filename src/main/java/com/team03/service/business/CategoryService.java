package com.team03.service.business;


import com.team03.entity.business.Category;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.CategoryMapper;
import com.team03.payload.mappers.CategoryPropertyKeyMapper;
import com.team03.entity.business.CategoryPropertyKey;
import com.team03.exception.BadRequestException;
import com.team03.payload.request.business.CategoryRequest;
import com.team03.payload.request.business.CategoryUpdateRequest;
import com.team03.payload.response.business.*;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.CategoryPropertyKeyRepository;
import com.team03.repository.business.CategoryRepository;
import com.team03.service.helper.MethodHelper;
import com.team03.service.helper.RestPage;
import com.team03.service.helper.SlugHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MethodHelper methodHelper;
    private final AdvertRepository advertRepository;
    private final CategoryPropertyKeyMapper categoryPropertyKeyMapper;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final SlugHelper slugHelper;

// Post
    public ResponseMessage<CategoryResponseWithoutPropertyKey> saveCategory(CategoryRequest categoryRequest) {

        isExistsByTitle(categoryRequest.getTitle()); // uniq control
        Category category =categoryMapper.requestToCategory(categoryRequest);
        String slug = slugHelper.createSlug(categoryRequest.getTitle());
        category.setSlug(slug);

//        //requestten cpk yi aldik
//        Set<CategoryPropertyKeyRequest> categoryPropertyKeyRequests = categoryRequest.getCategoryPropertyKeyRequests();
//        // bos bir set olusturduk
//        Set<CategoryPropertyKey> cpkSet = new HashSet<>();
//        // request ile gelen her bir CategoryPropertyKeyRequest objesini cpk ya cevirip cpkSet e attik.
//        if(!categoryPropertyKeyRequests.isEmpty()){
//            for (CategoryPropertyKeyRequest request : categoryPropertyKeyRequests) {
//                cpkSet.add(categoryPropertyKeyMapper.requestToCPK(request));
//            }
//            categoryPropertyKeyRepository.saveAll(cpkSet);
//            category.setCategoryPropertyKeys(cpkSet);
//        }
        Category savedCategory=categoryRepository.save(category);
        CategoryResponseWithoutPropertyKey responseWithoutPropertyKey=categoryMapper.CategoryToWithoutPropertyKeyResponse(savedCategory);

        return ResponseMessage.<CategoryResponseWithoutPropertyKey>builder()
                .message(MessageUtil.getMessage("category.create"))
                .httpStatus(HttpStatus.CREATED)
                .object(responseWithoutPropertyKey)
                .build();
    }

    // Put
    public ResponseMessage<CategoryUpdateResponse> updateById(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        // bu id ye sahip bir category yoksa hata mesaji dondurulecek
        Category category1=getCategoryById(id);
        // built in kontrolu
        isThisBuiltIn(category1.getBuiltIn());

        Category category = categoryMapper.requestToUpdateCategory(categoryUpdateRequest);
        String slug = slugHelper.createSlug(categoryUpdateRequest.getTitle());
        category.setSlug(slug);
        category.setCreateAt(category1.getCreateAt());
        category.setId(category1.getId());
        category.setCategoryPropertyKeys(category1.getCategoryPropertyKeys());
        categoryRepository.save(category);

        return ResponseMessage.<CategoryUpdateResponse>builder()
                .message(MessageUtil.getMessage("category.update"))
                .httpStatus(HttpStatus.CREATED)
                .object(categoryMapper.updateCategoryToResponse(category))
                .build();

    }
    //helper uniq control
    public void isExistsByTitle(String title){
        if(categoryRepository.existsByTitle(title)){
            throw new BadRequestException(MessageUtil.getMessage("already.exists",title));
        }
    }

    // helper find id
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.by.id",categoryId)));
    }

    // helper built in control
    public void isThisBuiltIn(Boolean built_in) {
        if (built_in) {
            throw new BadRequestException(MessageUtil.getMessage("built.in.value.error"));
        }
    }


    //get categories
    public ResponseMessage<Page<CategoryResponse>> getCategories(String query, int page, int size, String sort, String type) {
        if(query!=null){
            query=query.trim();
        }
        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);
        Page<Category> categories = categoryRepository.findByQueryIsActiveTrue(query, pageable);

        return getListResponseMessage(query, categories);
    }

    //get categories/admin
    public ResponseMessage<Page<CategoryResponse>> getCategoriesForAdmin(String q, int page, int size, String sort, String type) {

        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);
        Page<Category> categories=categoryRepository.findByQuery(q,pageable);
        return getListResponseMessage(q, categories);
    }

    //helper No Content
    private ResponseMessage<Page<CategoryResponse>> getListResponseMessage(String q, Page<Category> categories) {
        if (categories.isEmpty()) {//query ile birsey bulunumazsa no content hatasi ve mesaj
            return ResponseMessage.<Page<CategoryResponse>>builder()
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message(MessageUtil.getMessage("not.found.by.query",q))
                    .build();
        }
        return ResponseMessage.<Page<CategoryResponse>> builder()
                .httpStatus(HttpStatus.OK)
                .object( new RestPage<> (categories.map(categoryMapper::CategoryToResponse)))
                .build();
    }

    // get category by id
    public ResponseMessage<CategoryResponse> selectCategoryById(Long id) {
       Category category= getCategoryById(id);//bu id ile category var mi?
       return ResponseMessage.<CategoryResponse>builder()
               .httpStatus(HttpStatus.OK)
               .object(categoryMapper.CategoryToResponse(category))
               .build();
    }

    //delete category by id
    public ResponseMessage<CategoryResponse> deleteById(Long categoryId) {
        Category category= getCategoryById(categoryId); //bu id ile category var mi?
        isThisBuiltIn(category.getBuiltIn());//!!! built in true ise hata mesaji firlatildi

        Boolean advert=advertRepository.existsByCategoryId(categoryId);
        if(advert){
            throw new BadRequestException(MessageUtil.getMessage("associated.delete.error"));
        }

        categoryRepository.deleteById(categoryId);
        return ResponseMessage.<CategoryResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("category.delete"))
                .object(categoryMapper.CategoryToResponse(category))
                .build();

    }

    //get properties by category id
    //tekrar bakilacak !!!!!!!!!!!!!!!!!!!!!!11
    public ResponseMessage<Set<CategoryPropertyKeyResponse>> getCategoryPropertiesWithCategoryById(Long id) {
        Category category=getCategoryById(id); //yoksa hata
        Set<CategoryPropertyKey> cpk = category.getCategoryPropertyKeys();
        Set<CategoryPropertyKeyResponse> responses=categoryPropertyKeyMapper.CPKToResponses(cpk, id);
       return ResponseMessage.<Set<CategoryPropertyKeyResponse>> builder()
               .httpStatus(HttpStatus.OK)
               .object(responses)
               .build();


    }
    //get properties by category slug
    public ResponseMessage<CategoryResponse> getCategoriesBySlug(String slug) {
        Category category=categoryRepository.findByCategoriesWithSlug(slug);
        if (category==null) {
            return ResponseMessage.<CategoryResponse>builder()
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message(MessageUtil.getMessage("not.found.by.query",slug))
                    .build();
        }
        return ResponseMessage.<CategoryResponse> builder()
                .httpStatus(HttpStatus.OK)
                .object(categoryMapper.CategoryToResponse(category))
                .build();
    }

    public void checkCategoryKeyById(Long categoryId, List<Long> keyIds) {
        Category category = getCategoryById(categoryId);

        if (category.getCategoryPropertyKeys() == null || keyIds == null) {
            throw new BadRequestException(MessageUtil.getMessage("no.match.category.and.key"));
        }

        Set<Long> categoryKeyIds = new HashSet<>();

        category.getCategoryPropertyKeys().forEach(categoryPropertyKey -> categoryKeyIds.add(categoryPropertyKey.getId()));

        for (Long keyId : keyIds) {
            if (!categoryKeyIds.contains(keyId)) {
                throw new BadRequestException(MessageUtil.getMessage("no.match.category.and.key"));
            }
        }

    }
}
