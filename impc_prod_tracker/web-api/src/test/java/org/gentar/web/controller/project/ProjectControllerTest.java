package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.gentar.biology.project.ProjectRepository;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.db.Paths;
import org.gentar.organization.person.PersonRepository;
import org.gentar.service.biology.project.ProjectService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

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
