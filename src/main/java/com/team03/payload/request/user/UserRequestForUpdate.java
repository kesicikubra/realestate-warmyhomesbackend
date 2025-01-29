package com.team03.payload.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserRequestForUpdate {
    @NotNull
    @Size(min = 2,max = 30,message = "{user.request.forupdate.firstname.size}")
    private String first_name;

    @NotNull
    @Size(min = 2,max = 30,message = "{user.request.forupdate.lastname.size}")
    private String last_name;

    @NotNull
    //@Pattern(regexp = " \\d{3}-\\d{3}-\\d{4}") // "###-###-####"
    private String phone_number;

    @NotNull
    @Size(min = 10,max = 80,message = "{user.request.forupdate.email.size}")
    @Email
    private String email;
}
