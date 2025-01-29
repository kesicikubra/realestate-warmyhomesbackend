package com.team03.payload.response.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthResponse implements Serializable {
    private String first_name;
    private String accessToken;
    private String refreshToken;
    private Set<String> role;
    private Long photoId;
}
