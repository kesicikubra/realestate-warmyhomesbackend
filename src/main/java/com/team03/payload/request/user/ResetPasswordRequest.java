package com.team03.payload.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotNull(message = "{reset.password.request.code.notnull}")
    private String code;
    @NotNull(message = "{reset.password.request.password.notnull}")
    @Size(min = 8, max = 60,message = "{reset.password.request.password.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String password;

}
