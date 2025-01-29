package com.team03.payload.request.user;

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
public class UserRequestForUpdateRole {


    @NotNull(message = "{user.request.forupdate.role.userrole.notnull}")
    private Set<String> user_roles;

}
