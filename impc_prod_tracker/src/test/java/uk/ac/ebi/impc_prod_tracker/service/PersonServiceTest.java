package uk.ac.ebi.impc_prod_tracker.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.constants.PersonManagementConstants;
import uk.ac.ebi.impc_prod_tracker.conf.security.jwt.JwtTokenProvider;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.InstituteRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;
import uk.ac.ebi.impc_prod_tracker.domain.login.UserRegisterRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PersonServiceTest
{
    @Mock
    private PersonRepository personRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private WorkUnitRepository workUnitRepository;
    @Mock
    private InstituteRepository instituteRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private static final String NAME_TEST = "name";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String WORK_UNIT_NAME = "workUnitName";
    private static final String INSTITUTE_NAME = "instituteName";
    private static final Set<String> INSTITUTE_NAME_SET = new HashSet<>(Arrays.asList(INSTITUTE_NAME));
    private static final String ROLE_NAME = "roleName";
    private static final Role ROLE_TEST = new Role(ROLE_NAME);
    private static final WorkUnit WORK_UNIT_TEST = new WorkUnit(WORK_UNIT_NAME);
    private static final String AUTH_ID = "auth_id";

    private static final ResponseEntity<String> USER_CREATED_IN_AAP_RESPONSE
        = new ResponseEntity<>(AUTH_ID, HttpStatus.OK);
    private static final Institute INSTITUTE = new Institute(INSTITUTE_NAME);
    private static UserRegisterRequest userRegisterRequest;

    private PersonService testInstance;

    @Before
    public void setUp() throws Exception
    {
        testInstance = new PersonService(
            personRepository,
            roleRepository,
            workUnitRepository,
            instituteRepository,
            restTemplate, jwtTokenProvider);

        userRegisterRequest =
            new UserRegisterRequest(
                NAME_TEST,
                PASSWORD,
                EMAIL,
                WORK_UNIT_NAME,
                INSTITUTE_NAME_SET,
                ROLE_NAME);

        when(personRepository.findPersonByEmail(EMAIL)).thenReturn(null);
        when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(ROLE_TEST);
        when(workUnitRepository.findWorkUnitByIlarCode(WORK_UNIT_NAME)).thenReturn(WORK_UNIT_TEST);
        when(restTemplate.postForEntity(
            eq(PersonManagementConstants.LOCAL_AUTHENTICATION_URL), any(), eq(String.class)))
            .thenReturn(USER_CREATED_IN_AAP_RESPONSE);
        when(instituteRepository.findInstituteByName(INSTITUTE_NAME)).thenReturn(INSTITUTE);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testCreatePerson()
    {
        Person p = testInstance.createPerson(userRegisterRequest);

        assertEquals(AUTH_ID, p.getAuthId());
        assertEquals(EMAIL, p.getEmail());
    }

    @Test
    public void testCreatePersonWhenPersonAlreadyExists()
    {
        Person existingPerson = new Person(EMAIL);
        when(personRepository.findPersonByEmail(EMAIL)).thenReturn(existingPerson);

        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(String.format(PersonService.PERSON_ALREADY_EXISTS_ERROR, EMAIL));

        testInstance.createPerson(userRegisterRequest);
    }

    @Test
    public void testCreatePersonWhenPersonAlreadyExistsInAap()
    {
        when(personRepository.findPersonByEmail(EMAIL)).thenReturn(null);
        when(restTemplate.postForEntity(
            eq(PersonManagementConstants.LOCAL_AUTHENTICATION_URL), any(), eq(String.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));

        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(String.format(PersonService.PERSON_ALREADY_IN_AAP_ERROR, NAME_TEST));

        testInstance.createPerson(userRegisterRequest);
    }

    @Test
    public void testCreatePersonWithNotExistingWorkUnit()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(
            String.format(PersonService.WORK_UNIT_NOT_EXIST_IN_THE_SYSTEM, WORK_UNIT_NAME));
        when(workUnitRepository.findWorkUnitByIlarCode(WORK_UNIT_NAME)).thenReturn(null);

        testInstance.createPerson(userRegisterRequest);
    }

    @Test
    public void testCreatePersonWithNotExistingInstitute()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(
            String.format(PersonService.INSTITUTE_NOT_EXIST_IN_THE_SYSTEM, INSTITUTE_NAME));
        when(instituteRepository.findInstituteByName(INSTITUTE_NAME)).thenReturn(null);

        testInstance.createPerson(userRegisterRequest);
    }

    @Test
    public void testCreatePersonWithNotExistingRole()
    {
        exceptionRule.expect(OperationFailedException.class);
        exceptionRule.expectMessage(
            String.format(PersonService.ROLE_NOT_EXIST_IN_THE_SYSTEM, ROLE_NAME));
        when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(null);

        testInstance.createPerson(userRegisterRequest);
    }
}