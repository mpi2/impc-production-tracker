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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AAPService {
    private final RestTemplate restTemplate;

    private final PersonRepository personRepository;

    @Value("${local_authentication_base_url}")
    private String EXTERNAL_SERVICE_URL;

    @Value("${reset_password_token_user}")
    private String RESET_PASSWORD_TOKEN_USER;

    @Value("${reset_password_token_password}")
    private String RESET_PASSWORD_TOKEN_PASSWORD;

    private static final String AUTHENTICATION_ERROR = "Invalid userName/password provided.";

    private static final String EXPIRED_PASSWORD_ERROR = "Your password is expired. Please rest your password.";

    private static final String NON_PRODUCTION_OPERATION_ERROR = "This operation must be performed on the production service.";

    public AAPService(RestTemplate restTemplate, PersonRepository personRepository) {
        this.restTemplate = restTemplate;
        this.personRepository = personRepository;
    }

    /**
     * Returns a JWT given a userName and password. To do so, this calls the AAP system with the
     * provided data.
     *
     * @param userName User name.
     * @param password Password.
     * @return String with the JWT.
     */
    public String getToken(String userName, String password) throws JsonProcessingException {


        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", userName);
        form.add("password", password);
        form.add("grant_type", "password");
        form.add("client_id", "gentar-api");


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        try {
            // For some reason, using the injected restTemplate causes an odd behaviour. After
            // a successful login, if the user tries again the login process with an invalid
            // password, the call to the AAP keeps generating a valid token, when the expected
            // behaviour is to return an error message due to invalid credentials.
            // Using a new RestTemplate fixes the issue but it's not clear why.
            RestTemplate restTemplate = new RestTemplate();

            response = restTemplate.exchange(
                    EXTERNAL_SERVICE_URL + "realms/gentar/protocol/openid-connect/token",
                    HttpMethod.POST,
                    entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("Account is not fully set up")) {
                throw new BadCredentialsException(EXPIRED_PASSWORD_ERROR);
            } else {
                throw new BadCredentialsException(AUTHENTICATION_ERROR);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        // Extract the public_key

        return jsonNode.get("access_token").asText();
    }

    /**
     * Calls the endpoint in AAP for requesting the reset of the password for the user with email 'email'.
     * This will send an email to the user with a link to change the password.
     *
     * @param email Email of the user who needs the password reset.
     */
    public void requestPasswordReset(String email) throws JsonProcessingException {

        String body = "[\"UPDATE_PASSWORD\"]";

        String token = getToken(RESET_PASSWORD_TOKEN_USER, RESET_PASSWORD_TOKEN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        // Build the URL with query parameters
        String redirectUri = "https://www.gentar.org";
        String clientId = "gentar-api"; // Replace with the actual client ID

        Person person = personRepository.findPersonByEmail(email);


        String RESET_PASSWORD = "/execute-actions-email";
        String url = UriComponentsBuilder.fromHttpUrl(EXTERNAL_SERVICE_URL)
                .path("/admin/realms/gentar/users/" + person.getAuthId() + RESET_PASSWORD)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("client_id", clientId)
                .toUriString();

        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    }

    /**
     * Change the password for a user.
     *
     * @param email Email of the user.
     */
    public void changePassword(String email) throws JsonProcessingException {

        try {
            requestPasswordReset(email);
        } catch (HttpClientErrorException hcee) {
            throw new UserOperationFailedException(
                    "Unauthorized access.", "Check that your current credentials are correct.");

        }
    }

}

