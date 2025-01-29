package com.team03.controller.buiness;


import com.team03.payload.request.business.CategoryRequest;
import com.team03.payload.request.business.CategoryUpdateRequest;
import com.team03.payload.response.business.*;
import com.team03.service.business.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    @Operation(tags = "Category", summary = "C04",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryResponseWithoutPropertyKey> saveCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.saveCategory(categoryRequest);

    }

    @PutMapping("/{id}")
    @Operation(tags = "Category", summary = "C05",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryUpdateResponse> updateById(@PathVariable Long id,
                                                              @RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        return categoryService.updateById(id, categoryUpdateRequest);

    }

    //get All categories
    @GetMapping()
    @Operation(tags = "Category", summary = "C01")
    public ResponseMessage<Page<CategoryResponse>>getCategories(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "sort",defaultValue = "id", required = false) String sort,
            @RequestParam(value = "type",defaultValue = "asc", required = false) String type
            ) {
        return categoryService.getCategories(query,page,size,sort,type);

    }

    //get categories for admin
    @GetMapping("/admin") //---> sort kismini seq degerine gore yapip,page sayisini 10 a dusurdum
    @Operation(tags = "Category", summary = "C02",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<Page<CategoryResponse>>getCategoriesForAdmin(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "sort",defaultValue = "seq", required = false) String sort,
            @RequestParam(value = "type",defaultValue = "asc", required = false) String type
    ) {
        return categoryService.getCategoriesForAdmin(query,page,size,sort,type);
    }

    //get by id category
    @GetMapping("/{id:[1-9]\\d*}")
    @Operation(tags = "Category", summary = "C03")
    public ResponseMessage<CategoryResponse> getCategoryById(@PathVariable Long id){
        return categoryService.selectCategoryById(id);
    }

    // delete category
    @DeleteMapping("/{id}")
    @Operation(tags = "Category", summary = "C06",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryResponse> deleteById(@PathVariable("id") Long id){
        return categoryService.deleteById(id);
    }

    //get properties by category id
    @GetMapping("/{id}/properties")
    @Operation(tags = "Category", summary = "C07")
    public ResponseMessage<Set<CategoryPropertyKeyResponse>> getCategoryPropertiesWithCategoryById(@PathVariable Long id){
        return categoryService.getCategoryPropertiesWithCategoryById(id);
    }

    @GetMapping("/{slug:[a-zA-Z]+[a-zA-Z0-9]*[a-zA-Z]+}")
    @Operation(tags = "Category", summary = "C11")
    public ResponseMessage<CategoryResponse> getByWithSlug(@PathVariable("slug") String slug){
        return categoryService.getCategoriesBySlug(slug);
    }








}
