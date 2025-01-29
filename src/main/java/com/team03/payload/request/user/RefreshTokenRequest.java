package com.team03.payload.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotNull(message = "{refresh.token.request.refreshtoken.notnull}")
    private String refresh_token;
}
