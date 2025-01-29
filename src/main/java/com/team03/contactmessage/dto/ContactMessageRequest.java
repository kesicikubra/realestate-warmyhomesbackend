package com.team03.contactmessage.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactMessageRequest(
        @NotNull(message = "{error.firstname}")
        @Size(min = 3, max = 16, message = "{error.firstname.size}")
        @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "{error.firstname.pattern}")
        String firstName,

        @NotNull(message = "{error.lastname}")
        @Size(min = 3, max = 16, message = "{error.firstname.size}")
        @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "{error.lastname.pattern}")
        String lastName,

        @NotNull(message = "{error.email.notnull}")
        @Size(min = 5, max = 60, message = "{error.email.size}")
        @Email(message = "{error.email.pattern}")
        String email,

        @NotNull(message = "{error.message.notnull}")
        @Size(min = 4, max = 300, message = "{error.message.size}")
        @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "{error.message.pattern}")
        String message
) {
}
