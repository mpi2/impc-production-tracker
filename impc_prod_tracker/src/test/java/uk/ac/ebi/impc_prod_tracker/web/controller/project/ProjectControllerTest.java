package uk.ac.ebi.impc_prod_tracker.web.controller.project;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.ProjectRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends ControllerTestTemplate
{
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    @Test
    @DatabaseSetup("/dbunit/createUser.xml")
    public void test() throws Exception
    {
        System.out.println(personRepository);
        List<Person> people = personRepository.findAll();

        System.out.println("THE SIZE "+people.size());
        mvc().perform(get("/api/hello?mje=you"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello you")))
            .andDo(document("home"));
    }

    @Test
    @DatabaseSetup("/dbunit/projects/multipleProjects.xml")
    public void testGetAllProjects() throws Exception
    {
        mvc().perform(get("/api/projectSummaries"))
            .andExpect(status().isOk())
            .andDo(document("projectSummaries"));

        List<Project> projects = projectService.getProjects();
        assertEquals(2, projects.size());
        Project project1 = projectService.getProjectByTpn("TPN01");
        assertNotNull(project1);
        Project project2 = projectService.getProjectByTpn("TPN02");
        assertNotNull(project2);
    }

}
