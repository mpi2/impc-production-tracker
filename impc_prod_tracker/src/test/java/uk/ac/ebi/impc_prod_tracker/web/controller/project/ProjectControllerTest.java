package uk.ac.ebi.impc_prod_tracker.web.controller.project;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.ProjectRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerTestTemplate
{
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    @Test
    @DatabaseSetup("/dbunit/projects/multipleProjects.xml")
    void testGetAllProjects() throws Exception
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
