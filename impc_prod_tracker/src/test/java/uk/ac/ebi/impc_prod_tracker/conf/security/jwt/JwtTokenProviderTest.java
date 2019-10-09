package uk.ac.ebi.impc_prod_tracker.conf.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.AapSystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.PublicKeyProvider;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest
{
    private JwtTokenProvider testInstance;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

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
    @Before
    public void setUp() throws Exception
    {
        claims = buildClaims();
        testInstance = new JwtTokenProvider(publicKeyProvider, systemSubject);
        testInstance = spy(testInstance);

        AapSystemSubject aapSystemSubject = buildSubject(claims);

        when(systemSubject.buildSystemSubjectByClaims(claims)).thenReturn(aapSystemSubject);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testGetClaims()
    {
        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        String token = generateToken();
        Claims claims = testInstance.getClaims(token);
        List<String> domains = (List<String>) claims.get("domains");

        assertEquals("Unexpected subject", SUBJECT_NAME, claims.getSubject());
        assertEquals("Unexpected nickname", LOGIN, claims.get("nickname"));
        assertEquals("Unexpected email", EMAIL, claims.get("email"));
        assertEquals("Unexpected number of domains", 2, domains.size());
        assertEquals("Unexpected domains", DOMAINS.get(0), domains.get(0));
        assertEquals("Unexpected domains", DOMAINS.get(1), domains.get(1));
    }

    @Test
    public void testGetAuthentication()
    {
        doReturn(claims).when(testInstance).getClaims(VALID_TOKEN);

        Authentication authentication = testInstance.getAuthentication(VALID_TOKEN);

        assertTrue(authentication.getPrincipal() instanceof AapSystemSubject);
        AapSystemSubject subject = (AapSystemSubject) authentication.getPrincipal();
        assertEquals("Error", SUBJECT_NAME, subject.getUserRefId());
    }

    @Test
    public void testGetAuthenticationWithNullToken()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(JwtTokenProvider.NULL_EMPTY_TOKEN_MESSAGE);

        testInstance.getAuthentication(null);
    }

    @Test
    public void testGetAuthenticationWithEmptyToken()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(JwtTokenProvider.NULL_EMPTY_TOKEN_MESSAGE);

        testInstance.getAuthentication("");
    }

    @Test
    public void testGetAuthenticationWithMalformedToken()
    {
        exceptionRule.expect(io.jsonwebtoken.MalformedJwtException.class);

        testInstance.getAuthentication("x");
    }

    @Test
    public void testResolveTokenWhenBearerTokenOk()
    {
        String content = AUTHORIZATION_SCHEMA_NAME + VALID_TOKEN;
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn(content);
        String token = testInstance.resolveToken(req);

        assertEquals("Unexpected token", VALID_TOKEN, token);
    }

    @Test
    public void testResolveTokenWhenBearerTokenIsNull()
    {
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);
        String token = testInstance.resolveToken(req);

        assertNull(token);
    }

    @Test
    public void testResolveTokenWhenAuthenticationIsNotBearer()
    {
        when(req.getHeader(AUTHORIZATION_HEADER)).thenReturn("NO_BEARER");
        String token = testInstance.resolveToken(req);

        assertNull(token);
    }

    @Test
    public void testValidateToken()
    {
        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        String token = generateToken();
        testInstance.validateToken(token);
    }

    @Test
    public void testValidateTokenWhenExpired()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(JwtTokenProvider.INVALID_TOKEN_MESSAGE);

        doReturn(signingKeyResolver).when(testInstance).getSigningKeyResolver();

        Date exp = new Date(System.currentTimeMillis() - (1000 * 30)); // 30 seconds
        String token =  generateToken(exp);
        testInstance.validateToken(token);
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

        String token = Jwts.builder()
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

        return token;
    }

    private static String generateToken()
    {
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30)); // 30 seconds
        return generateToken(exp);
    }

    private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter()
    {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims)
        {
            return secretBytes;
        }
    };
}