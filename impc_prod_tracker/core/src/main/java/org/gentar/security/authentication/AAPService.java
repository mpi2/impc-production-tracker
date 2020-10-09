/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.security.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.util.JsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.gentar.organization.person.Person;
import java.nio.charset.Charset;

@Component
public class AAPService
{
    private final RestTemplate restTemplate;
    @Value("${local_authentication_base_url}")
    private String EXTERNAL_SERVICE_URL;
    @Value("${gentar_domain_reference}")
    private String GENTAR_DOMAIN_REFERENCE;

    //  By default the AAP token issued is valid for 1 hour.
    //  The ttl attribute allows you to change this period, and
    //  the maximum value that can be specified is 3 hours (180 minutes).
    //  We decided to use a value of 3 hours in the absence of a token refresh mechanism
    //  that allows you to extend the valid period of an existing token.
    //  see: https://api.aai.ebi.ac.uk/docs/authentication/authentication.index.html#token-ttl

    private final String TTL_ATTRIBUTE = "ttl=180";
    private final String AUTHENTICATION_ENDPOINT = "/auth";
    private final String DOMAIN_ENDPOINT = "/domains/%s/%s/user";
    private final String RESET_PASSWORD = "/reset";

    public static final String PERSON_ALREADY_IN_AAP_ERROR = "The user [%s] already exists in the "
        + "Authentication System.";

    private static final String AUTHENTICATION_ERROR = "Invalid userName/password provided.";

    public AAPService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * Returns a JWT given a userName and password. To do so, this calls the AAP system with the
     * provided data.
     * @param userName User name.
     * @param password Password.
     * @return String with the JWT.
     */
    public String getToken(String userName, String password)
    {
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userName, password);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try
        {
            // For some reason, using the injected restTemplate causes an odd behaviour. After
            // a successful login, if the user tries again the login process with an invalid
            // password, the call to the AAP keeps generating a valid token, when the expected
            // behaviour is to return an error message due to invalid credentials.
            // Using a new RestTemplate fixes the issue but it's not clear why.
            RestTemplate restTemplate = new RestTemplate();

            response = restTemplate.exchange(
                EXTERNAL_SERVICE_URL + AUTHENTICATION_ENDPOINT + '?' + TTL_ATTRIBUTE,
                HttpMethod.GET,
                entity,
                String.class);
        }
        catch (HttpClientErrorException e)
        {
            throw new BadCredentialsException(AUTHENTICATION_ERROR);
        }
        return response.getBody();
    }

    /**
     * Creates an account in the AAP system for a person in the system.
     * @param person Person information which will be used to build the payload to call the
     *               AAP endpoint that creates the user in that system.
     * @param token Token to be able to authenticate in AAP before executing the creation task.
     * @return A string with the id in the AAP system for the user.
     */
    public String createUser(Person person, String token) throws JsonProcessingException
    {
        String authId = createLocalAccount(person);
        associateWithDomain(authId, token);
        return authId;
    }

    private String createLocalAccount(Person person) throws JsonProcessingException
    {
        LocalAccountInfo localAccountInfo =
            new LocalAccountInfo(person.getName(), person.getPassword(), person.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload = JsonHelper.toJson(localAccountInfo);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response;
        try
        {
            response =
                restTemplate.postForEntity(
                    EXTERNAL_SERVICE_URL + AUTHENTICATION_ENDPOINT,
                    requestEntity,
                    String.class);
        }
        catch (HttpClientErrorException e)
        {
            String message = e.getMessage();
            if (e.getStatusCode().equals(HttpStatus.CONFLICT))
            {
                message = String.format(PERSON_ALREADY_IN_AAP_ERROR, localAccountInfo.userName);
            }
            throw new UserOperationFailedException(message);
        }
        return response.getBody();
    }

    private void associateWithDomain(String authId, String token)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<LocalAccountInfo> requestEntity = new HttpEntity<>(headers);
        String domainAssociationUrl =
            EXTERNAL_SERVICE_URL + String.format(DOMAIN_ENDPOINT, GENTAR_DOMAIN_REFERENCE, authId);
        restTemplate.exchange(domainAssociationUrl, HttpMethod.PUT, requestEntity, Void.class);
    }

    /**
     * Calls the endpoint in AAP for requesting the reset of the password for the user with email 'email'.
     * This will send an email to the user with a link to change the password.
     * @param email Email of the user who needs the password reset.
     */
    public void requestPasswordReset(String email) throws JsonProcessingException
    {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.userName = email;
        passwordResetRequest.email = email;

        String payload = JsonHelper.toJson(passwordResetRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        String domainAssociationUrl = EXTERNAL_SERVICE_URL + RESET_PASSWORD;
        restTemplate.exchange(domainAssociationUrl, HttpMethod.POST, requestEntity, Void.class);
    }

    /**
     * Change the password for a user.
     * @param email Email of the user.
     * @param oldPassword Old password.
     * @param newPassword New password.
     * @throws JsonProcessingException
     */
    public void changePassword(String email, String oldPassword, String newPassword) throws JsonProcessingException
    {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.password = newPassword;

        String payload = JsonHelper.toJson(passwordChangeRequest);

        HttpHeaders headers = createBasicAuthorizationHeaders(email, oldPassword);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String changePasswordUrl = EXTERNAL_SERVICE_URL + AUTHENTICATION_ENDPOINT;

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // Need to use this customised factory because PATH method is not working well with
        // default configuration of injected restTemplate.
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        try
        {
            restTemplate.exchange(changePasswordUrl, HttpMethod.PATCH, requestEntity, Void.class);
        }
        catch (HttpClientErrorException hcee)
        {
            if (hcee.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
            {
                throw new UserOperationFailedException(
                    "Unauthorized access.", "Check that your current credentials are correct.");
            }
        }
    }

    private HttpHeaders createBasicAuthorizationHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }

    /**
     * Internal class to represent the payload needed to create the user in the AAP local account.
     * The "name" field is just for display purposes and can be changed in any moment. "username"
     * is the one is used to log into the system, so it cannot be changed and will be assigned with
     * the email, meaning the way to log into the system is using the email and the password.
     */
    static class LocalAccountInfo
    {
        @JsonProperty("username")
        String userName;

        @JsonProperty("name")
        String name;

        @JsonProperty("password")
        String password;

        @JsonProperty("email")
        String email;

        LocalAccountInfo(String name, String password, String email)
        {
            this.name = name;
            this.password = password;
            this.email = email;
            this.userName = email;
        }
    }

    private static class PasswordResetRequest
    {
        @JsonProperty("username")
        String userName;

        @JsonProperty("email")
        String email;
    }

    private static class PasswordChangeRequest
    {
        @JsonProperty("password")
        String password;
    }
}
