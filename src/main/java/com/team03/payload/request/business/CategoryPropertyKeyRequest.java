package com.team03.payload.request.business;

import com.team03.entity.enums.KeyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryPropertyKeyRequest {


    @Size(min = 2, max = 80, message = "{category.property.key.request.name.size}")
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private KeyType keyType;

    private String unit;

}
