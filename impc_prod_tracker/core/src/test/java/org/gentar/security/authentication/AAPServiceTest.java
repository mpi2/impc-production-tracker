package org.gentar.security.authentication;

import static org.gentar.mockdata.MockData.personMockData;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.person.PersonRepository;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AAPServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PersonRepository personRepository;


    AAPService testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new AAPService(restTemplate, personRepository);

        ReflectionTestUtils.setField(testInstance, "EXTERNAL_SERVICE_URL", "");
        ReflectionTestUtils.setField(testInstance, "GENTAR_DOMAIN_REFERENCE", "value");

    }

    @Test
    void getToken() {

    }

    @Test
    void createUser() throws JsonProcessingException {

        ReflectionTestUtils.setField(testInstance, "EXPECTED_PRODUCTION_SERVICE_CONTEXT_PATH", "localhost:8080/tracker-api");
        ReflectionTestUtils.setField(testInstance, "serverServletContextPath", "localhost:8080/tracker-api");
        AAPService.LocalAccountInfo localAccountInfo =
                new AAPService.LocalAccountInfo(personMockData().getName(), personMockData().getPassword(), personMockData().getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload = JsonHelper.toJson(localAccountInfo);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "some response body",
                header,
                HttpStatus.OK
        );


        String AUTHENTICATION_ENDPOINT = "/auth";
        lenient().when(
                        restTemplate.postForEntity(
                                eq(AUTHENTICATION_ENDPOINT),
                                eq(requestEntity),
                                eq(String.class)))
                .thenReturn(responseEntity);
    }

    @Test
    void createUserNotWorkingWithProductionService() {


        ReflectionTestUtils.setField(testInstance, "EXPECTED_PRODUCTION_SERVICE_CONTEXT_PATH", "localhost:8080/tracker-api");
        ReflectionTestUtils.setField(testInstance, "serverServletContextPath", "localhost:8080/api");

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            String authId = testInstance.createUser(personMockData(), "testadmin");
        });

        String expectedMessage =
                "This operation must be performed on the production service.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void requestPasswordReset() {
    }

    @Test
    void changePassword() {
    }

}