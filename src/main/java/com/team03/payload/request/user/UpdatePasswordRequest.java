package com.team03.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotNull(message = "{update.password.request.oldpassword.notnull}")
    @Size(min = 8, max = 60,message = "{update.password.request.oldpassword.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String old_password;

    @NotBlank(message = "{update.password.request.newpassword.notblank}")
    @Size(min = 8, max = 60,message = "{update.password.request.newpassword.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String new_password;

}
