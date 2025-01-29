package com.team03.payload.response.user;

import com.team03.payload.response.abstracts.BaseUserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserResponseWithResetCode extends BaseUserResponse {
    private String reset_password_code;
}
