package com.team03.payload.request.business;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertTypeRequest {
    @NotNull(message = "{advert.type.request.title.notnull}")
    @Size(max =50, message = "{advert.type.request.title.size}")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "{advert.type.request.title.pattern}")
    private String title;
}
