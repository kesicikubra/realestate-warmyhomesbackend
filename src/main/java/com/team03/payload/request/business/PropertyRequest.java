package com.team03.payload.request.business;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PropertyRequest {

    @NotNull(message = "{property.request.keyid.notnull}")
    private Long keyId;

    @NotNull(message = "{property.request.value.notnull}")
    private String value;


}
