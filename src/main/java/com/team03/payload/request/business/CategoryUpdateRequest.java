package com.team03.payload.request.business;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequest {
    @NotNull(message = "{category.update.request.title.notnull}")
    @Size(max =150, message = "{category.update.request.title.size}")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "{category.update.request.title.pattern}")
    private String title;

    @Size(max =50, message = "{category.update.request.icon.size}")
    private String icon;

    @NotNull(message = "{category.update.request.seq.notnull}")
    private Integer seq;

    private Boolean isActive;
}
