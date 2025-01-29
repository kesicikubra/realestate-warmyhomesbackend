package com.team03.payload.request.user;

import com.team03.payload.request.abstracts.BaseUserRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserRequest extends BaseUserRequest {

    @NotNull(message = "{user.request.user.roles}")
    private Set<String> user_roles;

}
