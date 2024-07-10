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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.util.JsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Component
public class AAPService {
    private final RestTemplate restTemplate;

    private final PersonRepository personRepository;

    @Value("${local_authentication_base_url}")
    private String EXTERNAL_SERVICE_URL;

    @Value("${gentar_domain_reference}")
    private String GENTAR_DOMAIN_REFERENCE;

    @Value("${local_production_service_base_url:/tracker-api}")
    private String EXPECTED_PRODUCTION_SERVICE_CONTEXT_PATH;

    @Value("${server.servlet.context-path:root}")
    private String serverServletContextPath;

    //  By default the AAP token issued is valid for 1 hour.
    //  The ttl attribute allows you to change this period, and
    //  the maximum value that can be specified is 3 hours (180 minutes).
    //  We decided to use a value of 3 hours in the absence of a token refresh mechanism
    //  that allows you to extend the valid period of an existing token.

    private final String TTL_ATTRIBUTE = "ttl=180";
    private final String AUTHENTICATION_ENDPOINT = "/auth";
    private final String DOMAIN_ENDPOINT = "/domains/%s/%s/user";
    private final String RESET_PASSWORD = "/execute-actions-email";

    public static final String PERSON_ALREADY_IN_AAP_ERROR = "The user [%s] already exists in the "
        + "Authentication System.";

    private static final String AUTHENTICATION_ERROR = "Invalid userName/password provided.";

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
            throw new BadCredentialsException(AUTHENTICATION_ERROR);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        // Extract the public_key

        return jsonNode.get("access_token").asText();
    }

    /**
     * Creates an account in the AAP system for a person in the system.
     *
     * @param person Person information which will be used to build the payload to call the
     *               AAP endpoint that creates the user in that system.
     * @param token  Token to be able to authenticate in AAP before executing the creation task.
     * @return A string with the id in the AAP system for the user.
     */
    public String createUser(Person person, String token) throws JsonProcessingException
    {
        if (notWorkingWithProductionService()){
            throw new UserOperationFailedException(NON_PRODUCTION_OPERATION_ERROR);
        }
        person.setEmail(person.getEmail().toLowerCase());
        String authId = createLocalAccount(person);
        associateWithDomain(authId, token);
        if (isPersonAManager(person)) {
            addUserAsDomainManager(authId, token);
        }
        return authId;
    }

    private boolean isPersonAManager(Person person) {
        for (PersonRoleWorkUnit personRoleWorkUnit : person.getPersonRolesWorkUnits()) {
            if ("manager".equals(personRoleWorkUnit.getRole().getName())) {
                return true;
            }
        }
        for (PersonRoleConsortium personRoleConsortium : person.getPersonRolesConsortia()) {
            if ("manager".equals(personRoleConsortium.getRole().getName())) {
                return true;
            }
        }
        return false;
    }

    private String createLocalAccount(Person person) throws JsonProcessingException {
        LocalAccountInfo localAccountInfo =
                new LocalAccountInfo(person.getName(), person.getPassword(), person.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload = JsonHelper.toJson(localAccountInfo);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response;
        try {
            response =
                    restTemplate.postForEntity(
                            EXTERNAL_SERVICE_URL + AUTHENTICATION_ENDPOINT,
                            requestEntity,
                            String.class);
        } catch (HttpClientErrorException e) {
            String message = e.getMessage();
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                message = String.format(PERSON_ALREADY_IN_AAP_ERROR, localAccountInfo.userName);
            }
            throw new UserOperationFailedException(message);
        }
        return response.getBody();
    }

    private void associateWithDomain(String authId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<LocalAccountInfo> requestEntity = new HttpEntity<>(headers);
        String domainAssociationUrl =
                EXTERNAL_SERVICE_URL + String.format(DOMAIN_ENDPOINT, GENTAR_DOMAIN_REFERENCE, authId);
        restTemplate.exchange(domainAssociationUrl, HttpMethod.PUT, requestEntity, Void.class);
    }

    private void addUserAsDomainManager(String authId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<LocalAccountInfo> requestEntity = new HttpEntity<>(headers);
        String DOMAIN_MANAGER_ENDPOINT = "/domains/%s/managers/%s";
        String domainAssociationUrl =
                EXTERNAL_SERVICE_URL + String.format(DOMAIN_MANAGER_ENDPOINT, GENTAR_DOMAIN_REFERENCE, authId);
        restTemplate.exchange(domainAssociationUrl, HttpMethod.PUT, requestEntity, Void.class);
    }

    /**
     * Calls the endpoint in AAP for requesting the reset of the password for the user with email 'email'.
     * This will send an email to the user with a link to change the password.
     *
     * @param email Email of the user who needs the password reset.
     */
    public void requestPasswordReset(String email) throws JsonProcessingException {

        if (notWorkingWithProductionService()){
            throw new UserOperationFailedException(NON_PRODUCTION_OPERATION_ERROR);
        }

        String body = "[\"UPDATE_PASSWORD\"]";

        String token = getToken("", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        // Build the URL with query parameters
        String redirectUri = "https://www.gentar.org";
        String clientId = "gentar-api"; // Replace with the actual client ID

        Person person = personRepository.findPersonByEmail(email);


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
        if (notWorkingWithProductionService()) {
            throw new UserOperationFailedException(NON_PRODUCTION_OPERATION_ERROR);
        }

        try {
            requestPasswordReset(email);
        } catch (HttpClientErrorException hcee) {
            throw new UserOperationFailedException(
                    "Unauthorized access.", "Check that your current credentials are correct.");

        }
    }

    private Boolean notWorkingWithProductionService() {
        if (serverServletContextPath.equals(EXPECTED_PRODUCTION_SERVICE_CONTEXT_PATH)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }

    }

    private HttpHeaders createBasicAuthorizationHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII), true);
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    /**
     * Internal class to represent the payload needed to create the user in the AAP local account.
     * The "name" field is just for display purposes and can be changed in any moment. "username"
     * is the one is used to log into the system, so it cannot be changed and will be assigned with
     * the email, meaning the way to log into the system is using the email and the password.
     */
    static class LocalAccountInfo {
        @JsonProperty("username")
        String userName;

        @JsonProperty("name")
        String name;

        @JsonProperty("password")
        String password;

        @JsonProperty("email")
        String email;

        LocalAccountInfo(String name, String password, String email) {
            this.name = name;
            this.password = password;
            this.email = email;
            this.userName = email;
        }
    }
}

