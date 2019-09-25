package uk.ac.ebi.impc_prod_tracker.conf.security;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationHeaderReader
{
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_SCHEMA_NAME = "Bearer ";

    /**
     * Obtain the token from the Authorization header.
     * @param req Request.
     * @return String with the token.
     */
    public String getAuthorizationToken(HttpServletRequest req)
    {
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        String token = null;
        if (bearerToken != null && bearerToken.startsWith(AUTHORIZATION_SCHEMA_NAME))
        {
            token = bearerToken.substring(AUTHORIZATION_SCHEMA_NAME.length());
        }
        return token;
    }
}
