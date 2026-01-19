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
import org.gentar.biology.mutation.formatter.MutationFormatterServiceImpl;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.organization.person.keycloakUserCheck.KeycloakUserCheck;
import org.gentar.organization.person.keycloakUserCheck.KeycloakUserCheckRepository;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AAPService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MutationFormatterServiceImpl.class);

    private final RestTemplate restTemplate;

    private final PersonRepository personRepository;

    private final KeycloakUserCheckRepository keycloakUserCheckRepository;

    @Value("${local_authentication_base_url}")
    private String EXTERNAL_SERVICE_URL;

    @Value("${reset_password_token_user}")
    private String RESET_PASSWORD_TOKEN_USER;

    @Value("${reset_password_token_password}")
    private String RESET_PASSWORD_TOKEN_PASSWORD;

    private static final String AUTHENTICATION_ERROR = "Invalid userName/password provided.";

    private static final String EXPIRED_PASSWORD_ERROR = "Your password is expired. Please rest your password.";

    private static final String NON_PRODUCTION_OPERATION_ERROR = "This operation must be performed on the production service.";

    public AAPService(RestTemplate restTemplate, PersonRepository personRepository, KeycloakUserCheckRepository keycloakUserCheckRepository) {
        this.restTemplate = restTemplate;
        this.personRepository = personRepository;
        this.keycloakUserCheckRepository = keycloakUserCheckRepository;
    }

    /**
     * Returns a JWT given a userName and password. To do so, this calls the AAP system with the
     * provided data.
     *
     * @param email    User name.
     * @param password Password.
     * @return String with the JWT.
     */
    public String getToken(String email, String password) throws JsonProcessingException {

        Person person = personRepository.findPersonByEmail(email);
        if (person == null) {
            throw new BadCredentialsException(AUTHENTICATION_ERROR);
        }
        KeycloakUserCheck keycloakUserCheck = keycloakUserCheckRepository.findByUserName(person.getEmail());
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", email);
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

            if (keycloakUserCheck != null) {
                throw new BadCredentialsException(EXPIRED_PASSWORD_ERROR);
            } else {
                throw new BadCredentialsException(AUTHENTICATION_ERROR);
            }
        }


        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response.getBody());


        return jsonNode.get("access_token").asText();
    }

    /**
     * Returns a JWT for a service account without requiring a Person to exist in the database.
     * This is used for operations like password reset that require a service account token.
     *
     * @param email    Service account user name.
     * @param password Service account password.
     * @return String with the JWT.
     */
    private String getServiceToken(String email, String password) throws JsonProcessingException {
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", email);
        form.add("password", password);
        form.add("grant_type", "password");
        form.add("client_id", "gentar-api");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        try {
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
    public String createUser(Person person, String token) throws JsonProcessingException {
        person.setEmail(person.getEmail().toLowerCase());
        return addUserToKeycloak(person, token);
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


    public String addUserToKeycloak(Person user, String token) throws JsonProcessingException {
        String authId = "";
        UserRepresentation keycloakUser = mapToKeycloakUser(user);
        String url = EXTERNAL_SERVICE_URL + "admin/realms/gentar/users";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserRepresentation> request = new HttpEntity<>(keycloakUser, headers);

        try {
            LOGGER.info("Creating user: {}", keycloakUser.getEmail());
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("User created successfully: {}", user.getEmail());

                // Extract authId from Location header
                String locationHeader = response.getHeaders().getLocation().toString();
                if (locationHeader != null) {
                    authId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                }
            } else {
                LOGGER.info("Failed to create user: {}. Status: {}", user.getEmail(), response.getStatusCode());
                LOGGER.info("Response body: {}", response.getBody());
            }
        } catch (Exception e) {
            LOGGER.info("Exception while creating user: {}", keycloakUser.getEmail(), e);
        }

        return authId;
    }

    /**
     * Calls the endpoint in AAP for requesting the reset of the password for the user with email 'email'.
     * This will send an email to the user with a link to change the password.
     *
     * @param email Email of the user who needs the password reset.
     */
    public void requestPasswordReset(String email) throws JsonProcessingException {

        String body = "[\"UPDATE_PASSWORD\"]";

        String token = getServiceToken(RESET_PASSWORD_TOKEN_USER, RESET_PASSWORD_TOKEN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        // Build the URL with query parameters
        String redirectUri = "https://www.gentar.org";
        String clientId = "gentar-api"; // Replace with the actual client ID

        Person person = personRepository.findPersonByEmail(email);
        if (person == null || person.getAuthId() == null) {
            throw new UserOperationFailedException(
                    "User with email " + email + " not found or not properly configured.");
        }

        String RESET_PASSWORD = "/execute-actions-email";
        String url = UriComponentsBuilder.fromUriString(EXTERNAL_SERVICE_URL)
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

    private UserRepresentation mapToKeycloakUser(Person user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getName());
        userRepresentation.setLastName(user.getName());
        userRepresentation.setEnabled(user.getIsActive());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(user.getPassword());
        userRepresentation.setCredentials(List.of(passwordCred));

        userRepresentation.setRealmRoles(List.of("user"));

        return userRepresentation;
    }

}


