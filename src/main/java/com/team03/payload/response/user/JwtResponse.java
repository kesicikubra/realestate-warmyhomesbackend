package com.team03.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JwtResponse {
    private String access_token;
    private String token;
    private String first_name;
    private String last_name;

}
