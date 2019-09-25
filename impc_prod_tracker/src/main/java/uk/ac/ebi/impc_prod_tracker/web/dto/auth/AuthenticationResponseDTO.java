package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthenticationResponseDTO
{
    private String userName;
    private String accessToken;
}

