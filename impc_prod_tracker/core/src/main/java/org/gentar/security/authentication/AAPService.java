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
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.gentar.organization.person.Person;

@Component
public class AAPService
{
    private RestTemplate restTemplate;
    @Value("${local_authentication_url}")
    private String EXTERNAL_SERVICE_URL;

    public static final String PERSON_ALREADY_IN_AAP_ERROR = "The user [%s] already exists in the "
        + "Authentication System.";

    public AAPService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public String getToken(String userName, String password)
    {
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userName, password);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try
        {
            response = restTemplate.exchange(
                EXTERNAL_SERVICE_URL,
                HttpMethod.GET,
                entity,
                String.class);
        }
        catch (HttpClientErrorException e)
        {
            throw new BadCredentialsException(e.getMessage());
        }
        return response.getBody();
    }

    public String createUser(Person person)
    {
        String authId = createLocalAccount(person);
        return authId;
    }

    private String createLocalAccount(Person person)
    {
        LocalAccountInfo localAccountInfo =
            new LocalAccountInfo(person.getName(), person.getPassword(), person.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LocalAccountInfo> requestEntity = new HttpEntity<>(localAccountInfo, headers);
        ResponseEntity<String> response;
        try
        {
            response =
                restTemplate.postForEntity(
                    EXTERNAL_SERVICE_URL,
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
}
