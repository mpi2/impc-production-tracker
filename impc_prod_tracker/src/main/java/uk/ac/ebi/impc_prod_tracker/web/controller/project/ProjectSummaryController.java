package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.common.types.PlanTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.controller.common.PlanLinkBuilder;
import uk.ac.ebi.impc_prod_tracker.web.controller.util.LinkUtil;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectSummaryMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ProjectSummaryController
{
    private ProjectService projectService;
    private ProjectMapper projectMapper;
    private ProjectSummaryMapper projectSummaryMapper;
    private ProjectSpecs projectSpecs;

    ProjectSummaryController(
        ProjectService projectService,
        ProjectMapper projectMapper,
        ProjectSummaryMapper projectSummaryMapper,
        ProjectSpecs projectSpecs)
    {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.projectSummaryMapper = projectSummaryMapper;
        this.projectSpecs = projectSpecs;
    }

    @GetMapping(value = {"/projectSummaries/{tpn}"})
    public EntityModel<ProjectSummaryDTO> getProjectSummary(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);
        ProjectSummaryDTO projectSummaryDTO = getDTO(project);

        return new EntityModel<>(projectSummaryDTO);
    }

    @GetMapping(value = {"/projectSummaries"})
    public ResponseEntity findAll(Pageable pageable, PagedResourcesAssembler assembler)
    {
        Specification<Project> specification =
            Specification.where(projectSpecs.withPlansInUserWorkUnits());

        Page<Project> projects = projectService.getProjects(specification, pageable);
        Page<ProjectSummaryDTO> summaries =
            projects.map(this::getDTO);

        PagedModel pr =
            assembler.toModel(
                summaries,
                linkTo(ProjectSummaryController.class).slash("projectSummaries").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private ProjectSummaryDTO getDTO(Project project)
    {
        ProjectDTO projectDTO = projectMapper.toDto(project);
        ProjectSummaryDTO projectSummaryDTO = projectSummaryMapper.toDto(projectDTO);
        projectSummaryDTO.add(
            PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PRODUCTION, "production_plans"));
        projectSummaryDTO.add(
            PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PHENOTYPING, "phenotyping_plans"));
        return projectSummaryDTO;
    }
}
