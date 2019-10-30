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
package uk.ac.ebi.impc_prod_tracker.service.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.SystemOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.constants.PersonManagementConstants;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import java.util.HashMap;
import java.util.Map;

@Component
public class AAPService
{
    private RestTemplate restTemplate;

    public static final String PERSON_ALREADY_IN_AAP_ERROR = "The user [%s] already exists in the "
        + "Authentication System.";

    @Value("${local_authentication_base_url}")
    private String LOCAL_AUTHENTICATION_BASE_URL;

    @Value("${gentar-maintainer-domain-reference}")
    private String MAINTAINER_DOMAIN_REFERENCE;

    private static final String SET_USER_TO_DOMAIN =
        "%s/domains/{domainReference}/{userReference}/user";

    public AAPService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public String createUser(Person person, String token)
    {
        String authId = createLocalAccount(person);
        if (person.getEbiAdmin())
        {
            associateUserToDomain(authId, MAINTAINER_DOMAIN_REFERENCE, token);
        }
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
                    PersonManagementConstants.LOCAL_AUTHENTICATION_URL,
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

    public void associateUserToDomain(String userReference, String domainReference, String token)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Map<String, String> params = new HashMap<>();
        params.put("domainReference", domainReference);
        params.put("userReference", userReference);

        String url = String.format(SET_USER_TO_DOMAIN, LOCAL_AUTHENTICATION_BASE_URL);

        try
        {
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class, params);
        }
        catch (Exception e)
        {
            throw new SystemOperationFailedException(
                "Error associating user to domain", e.getMessage());
        }
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
