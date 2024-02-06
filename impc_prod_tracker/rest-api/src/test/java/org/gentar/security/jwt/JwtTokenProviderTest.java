package org.gentar.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.crypto.MacProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.gentar.exceptions.OperationFailedException;
import org.gentar.security.PublicKeyProvider;
import org.gentar.security.abac.subject.AapSystemSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class JwtTokenProviderTest
{
    private JwtTokenProvider testInstance;

    @Mock
    private PublicKeyProvider publicKeyProvider;

    @Mock
    private AapSystemSubject systemSubject;

    @Mock
    private HttpServletRequest req;

    private Claims claims;

    private static final String SUBJECT_NAME = "subjectTest";
    private static final String LOGIN = "login_test";
    private static final String EMAIL = "email_test";
    private static final List<String> DOMAINS = Arrays.asList("dom1", "dom2");

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_SCHEMA_NAME = "Bearer ";

    private static final String VALID_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2V4cGxvc" +
        "mUuYWFpLmViaS5hYy51ay9zcCIsImp0aSI6IjN3RWlBSE4ybXBsc1FaVkJocUgtcVEiLCJpYXQiOjE1NTM2MTA3N" +
        "zQsInN1YiI6InVzci05YTI3YjJkYy1mMjdlLTQ5NjItODlhYy01N2IyMTc1MmU4MTUiLCJlbWFpbCI6Im5ld2lta" +
        "XRzdXNlcjFAZXhhbXBsZS5jb20iLCJuaWNrbmFtZSI6Im5ld2ltaXRzdXNlcjEiLCJuYW1lIjoiTmV3IEltaXRzI" +
        "FVzZXIxIiwiZG9tYWlucyI6WyJzZWxmLmRvbWFpbi10ZXN0Il0sImV4cCI6MTU1MzYxNDM3NH0.XiHi3RYAgXmY3" +
        "So0G_srjAPfeLA7zU3lXdJw7onqq1C85jptong0MxO_9SCgtYdpQ8wlrGt4A73w7tKt-GfAFWpzdlBwkjYH2QUqk" +
        "whBLX_hFjvvlhJM7qwe8douWXAYmwe-DU_uQDx1lF2ntAGtIu6eeItSE9HKiCYAZ7yOZNpNM0rlCzQohFFezCxln" +
        "2U0U9ynit6Rox9rSkSCPkCraNZevtwC9HSo00mxGrSANYhRPhaOSDry42kL5rC7wZSu4JyzypjVi2BsLP2N_jRfh" +
        "nwlcZXAfP6NJKUaZP07te8NfGYz_w0cuDrjgeiAd64w_fHGTOmxTs8GY6tmeDR1Xw";


    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    @BeforeEach
    public void setUp() throws Exception
    {
        claims = buildClaims();
        testInstance = new JwtTokenProvider(publicKeyProvider, systemSubject);
        testInstance = spy(testInstance);

        AapSystemSubject aapSystemSubject = buildSubject(claims);

        when(systemSubject.buildSystemSubjectByPerson(any())).thenReturn(aapSystemSubject);
    }

    @AfterEach
    public void tearDown()
    {
    }

    @Test
    @SuppressWarnings("unchecked")
    @Disabled
    public void testGetClaims()
    {
        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        String token = generateToken();
        Claims claims = testInstance.getClaims(token);
        List<String> domains = (List<String>) claims.get("domains");

        assertEquals("Unexpected subject", SUBJECT_NAME, claims.getSubject());
        assertEquals("Unexpected domains", DOMAINS.get(0), domains.get(0));
        assertEquals("Unexpected domains", DOMAINS.get(1), domains.get(1));
    }

    @Test
    @Disabled
    public void testGetAuthentication()
    {
        doReturn(claims).when(testInstance).getClaims(VALID_TOKEN);

        Authentication authentication = testInstance.getAuthentication(VALID_TOKEN);

        assertInstanceOf(AapSystemSubject.class, authentication.getPrincipal());
        AapSystemSubject subject = (AapSystemSubject) authentication.getPrincipal();
        assertEquals("Error", SUBJECT_NAME, subject.getUserRefId());
    }

    @Test
    @Disabled
    public void testGetAuthenticationWithNullToken()
    {
        Exception exception = assertThrows(OperationFailedException.class, () ->
                testInstance.getAuthentication(null));
        assertEquals(JwtTokenProvider.NULL_EMPTY_TOKEN_MESSAGE, exception.getMessage());
    }

    @Test
    @Disabled
    public void testGetAuthenticationWithEmptyToken()
    {
        Exception exception = assertThrows(OperationFailedException.class, () ->
                testInstance.getAuthentication(""));
        assertEquals(JwtTokenProvider.NULL_EMPTY_TOKEN_MESSAGE, exception.getMessage());
    }

    @Test
    @Disabled
    public void testGetAuthenticationWithMalformedToken()
    {
        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () ->
                testInstance.getAuthentication("x"));
    }

    @Test
    @Disabled
    public void testResolveTokenWhenBearerTokenOk()
    {
        String content = AUTHORIZATION_SCHEMA_NAME + VALID_TOKEN;
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn(content);
        String token = testInstance.resolveToken(req);

        assertEquals("Unexpected token", VALID_TOKEN, token);
    }

    @Test
    @Disabled
    public void testResolveTokenWhenBearerTokenIsNull()
    {
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);
        String token = testInstance.resolveToken(req);

        assertNull(token);
    }

    @Test
    @Disabled
    public void testResolveTokenWhenAuthenticationIsNotBearer()
    {
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn("NO_BEARER");
        String token = testInstance.resolveToken(req);

        assertNull(token);
    }

    @Test
    @Disabled
    public void testValidateToken()
    {
        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        String token = generateToken();
        testInstance.validateToken(token);
    }

    @Test
    @Disabled
    public void testValidateTokenWhenExpired()
    {
        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        Date exp = new Date(System.currentTimeMillis() - (1000 * 30)); // 30 seconds
        String token =  generateToken(exp);

        Exception exception = assertThrows(OperationFailedException.class, () ->
                testInstance.validateToken(token));
        assertEquals(JwtTokenProvider.INVALID_TOKEN_MESSAGE, exception.getMessage());

    }

    private Claims buildClaims()
    {
        DefaultClaims defaultClaims = new DefaultClaims();
        defaultClaims.setSubject(SUBJECT_NAME);
        return defaultClaims;
    }

    private AapSystemSubject buildSubject(Claims claims)
    {
        AapSystemSubject aapSystemSubject = new AapSystemSubject();
        aapSystemSubject.setUserRefId(claims.getSubject());

        return aapSystemSubject;
    }

    private static String generateToken(Date exp)
    {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();

        return Jwts.builder()
            .setSubject(SUBJECT_NAME)
            .claim("nickname", LOGIN)
            .claim("email", EMAIL)
            .claim("domains", DOMAINS)
            .setId(id)
            .setIssuedAt(now)
            .setNotBefore(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, base64SecretBytes)
            .compact();
    }

    private static String generateToken()
    {
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30)); // 30 seconds
        return generateToken(exp);
    }

    private final SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter()
    {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims)
        {
            return secretBytes;
        }
    };
}