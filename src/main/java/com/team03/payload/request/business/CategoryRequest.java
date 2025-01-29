package com.team03.payload.request.business;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotNull(message = "{category.request.title.notnull}")
    @Size(max =150, message = "{category.request.title.size}")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "{category.request.title.pattern}")
    private String title;

    @NotNull(message = "{category.request.icon.notnull}")
    @Size(max =50, message = "{category.request.icon.size}")
    private String icon;

    @NotNull(message = "{category.request.seq.notnull}")
    private Integer seq;

    @NotNull
    private Boolean isActive;

//    private Set<CategoryPropertyKeyRequest> categoryPropertyKeyRequests;


}
