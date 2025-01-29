package com.team03.payload.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "{login.request.email.notnull}")
    @Size(min = 10,max = 80,message = "{login.request.email.size}")
    @Email
    private String email;

    @NotNull(message = "{login.request.password.notnull}")
    @Size(min = 8, max = 60,message = "{login.request.password.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String password;
}
