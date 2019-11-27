package org.gentar.security.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthenticationResponseDTO
{
    private String userName;
    private String accessToken;
}

