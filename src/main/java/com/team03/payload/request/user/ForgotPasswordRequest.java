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
public class ForgotPasswordRequest {

    @NotNull(message = "{forgot.password.request.email.notnull}")
    @Size(min = 10,max = 80,message = "{forgot.password.request.email.size}")
    @Email
    private String email;
}
