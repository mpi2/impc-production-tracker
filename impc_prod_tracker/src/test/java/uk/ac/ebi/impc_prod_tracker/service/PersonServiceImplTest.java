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
package uk.ac.ebi.impc_prod_tracker.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.ContextAwarePolicyEnforcement;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.service.conf.AAPService;
import uk.ac.ebi.impc_prod_tracker.service.organization.PersonService;
import uk.ac.ebi.impc_prod_tracker.service.organization.PersonServiceImpl;
import uk.ac.ebi.impc_prod_tracker.service.organization.management.ManagementService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PersonServiceImplTest
{
    @Mock
    private PersonRepository personRepository;

    @Mock
    private SubjectRetriever subjectRetriever;

    @Mock
    private SystemSubject systemSubject;

    @Mock
    private AAPService aapService;

    @Mock
    private ManagementService managementService;

    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;

    private List<Person> allThePeople = new ArrayList<>();

    private PersonService testInstance;

    @Before
    public void setup()
    {
        testInstance = new PersonServiceImpl(personRepository, aapService, subjectRetriever, managementService, policyEnforcement);
        setPeople();
    }

    private void setPeople()
    {
        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();
        allThePeople.add(p1);
        allThePeople.add(p2);
        allThePeople.add(p3);
    }

    @Test
    public void testGetAllPeopleWhenAdmin()
    {
        when(subjectRetriever.getSubject()).thenReturn(systemSubject);
        when(systemSubject.isAdmin()).thenReturn(true);
        when(personRepository.findAll()).thenReturn(allThePeople);

        List<Person> people = testInstance.getAllPeople();
        assertEquals("", allThePeople, people);
    }

    @Test
    public void testGetAllPeopleWhenNotAdmin()
    {
        when(subjectRetriever.getSubject()).thenReturn(systemSubject);
        when(systemSubject.isAdmin()).thenReturn(false);
        verify(personRepository, times(0)).findAll();

        List<Person> people = testInstance.getAllPeople();
        assertEquals(Collections.emptyList(), people);
    }
}