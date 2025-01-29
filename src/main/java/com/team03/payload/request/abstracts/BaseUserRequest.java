package com.team03.payload.request.abstracts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseUserRequest implements Serializable {

    @NotNull
    @Size(min = 2,max = 30,message = "{base.user.firstname.size}")
    private String first_name;

    @NotNull
    @Size(min = 2,max = 30,message = "{base.user.lastname.size}")
    private String last_name;

    @NotNull
    //@Pattern(regexp = " \\d{3}-\\d{3}-\\d{4}") // "###-###-####"
    private String phone_number;

    @NotNull
    @Size(min = 10,max = 80,message = "{base.user.email.size}")
    @Email
    private String email;

    @NotNull(message = "{base.user.password.notnull}")
    @Size(min = 8, max = 60,message = "{base.user.password.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String password;


}
