package com.team03.controller.buiness;

import com.team03.payload.request.business.CategoryPropertyKeyRequest;
import com.team03.payload.response.business.CategoryPropertyKeyResponse;

import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.CategoryPropertyKeyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPropertyKeyController {
    private final CategoryPropertyKeyService categoryPropertyKeyService;

    @PostMapping("{id}/properties")
    @Operation(tags = "Category Property Key", summary = "C08",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryPropertyKeyResponse> createCategoryPropertyKeyWithRequest(@PathVariable("id") Long id,
                                                                                 @RequestBody CategoryPropertyKeyRequest
                                                                             categoryPropertyKeyRequest){
        return categoryPropertyKeyService.saveCategoryPropertyKeyWithRequest(id,categoryPropertyKeyRequest);

    }

    @PutMapping("/properties/{id}")
    @Operation(tags = "Category Property Key", summary = "C09",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryPropertyKeyResponse> updateCategoryPropertyKey(@PathVariable Long id,
                                                                                  @RequestBody CategoryPropertyKeyRequest CPKRequest){
        return categoryPropertyKeyService.updateCategoryPropertyKey(id,CPKRequest);

    }

    @DeleteMapping("/properties/{id}")
    @Operation(tags = "Category Property Key", summary = "C10",description = "ADMIN MANAGER")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    ResponseMessage<CategoryPropertyKeyResponse> deleteCategoryPropertyKey(@PathVariable Long id){
        return categoryPropertyKeyService.deleteCategoryPropertyKeyById(id);

    }
}
