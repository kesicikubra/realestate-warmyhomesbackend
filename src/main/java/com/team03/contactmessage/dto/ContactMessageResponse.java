package com.team03.contactmessage.dto;

import com.team03.contactmessage.entity.Status;
import java.time.LocalDateTime;


public record ContactMessageResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String message,
        LocalDateTime createAt,
        Status status,
        String subject
) {
}
