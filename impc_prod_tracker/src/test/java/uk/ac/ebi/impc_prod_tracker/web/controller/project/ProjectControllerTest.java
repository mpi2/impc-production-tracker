package uk.ac.ebi.impc_prod_tracker.web.controller.project;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.ProjectRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import uk.ac.ebi.impc_prod_tracker.service.biology.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.db.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class ProjectControllerTest extends ControllerTestTemplate
{
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    @Ignore
    void testGetAllProjects() throws Exception
    {
        // TODO: Fix when new project structure finished.
//        mvc().perform(get("/api/projectSummaries"))
//            .andExpect(status().isOk())
//            .andDo(document("projectSummaries"));
//
//        List<Project> projects = projectService.getCurrentUserProjects();
//        assertEquals(2, projects.size());
//        Project project1 = projectService.getCurrentUserProjectByTpn("TPN01");
//        assertNotNull(project1);
//        Project project2 = projectService.getCurrentUserProjectByTpn("TPN02");
//        assertNotNull(project2);
    }

}
