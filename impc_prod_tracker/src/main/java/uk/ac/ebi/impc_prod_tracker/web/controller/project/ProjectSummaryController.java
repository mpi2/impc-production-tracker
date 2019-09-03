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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.controller.plan.PlanController;
import uk.ac.ebi.impc_prod_tracker.web.controller.util.LinkUtil;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ProjectSummaryController
{
    private ProjectService projectService;
    private PlanService planService;
    private ProjectMapper projectMapper;
    private PlanMapper planMapper;

    ProjectSummaryController(
        ProjectService projectService,
        PlanService planService,
        ProjectMapper projectMapper,
        PlanMapper planMapper)
    {
        this.projectService = projectService;
        this.planService = planService;
        this.projectMapper = projectMapper;
        this.planMapper = planMapper;
    }

    /**
     * Returns Information about the projects with pagination.
     *
     * @param markerSymbols Optional filters with specific marker symbols.
     * @param workUnits     Optional filters with specific workUnits names.
     * @param workGroups    Optional filters with specific marker names.
     * @param pageable      Pagination options.
     * @param assembler     Assembler to add links to the resources.
     * @return              List of ProjectSummaryDTO.
     */
    @GetMapping(value = {"/projectSummaries"})
    ResponseEntity getProjectSummariesPaginated(
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
        @RequestParam(value = "workUnit", required = false) List<String> workUnits,
        @RequestParam(value = "workGroup", required = false) List<String> workGroups,
        @RequestParam(value = "planType", required = false) List<String> planTypes,
        @RequestParam(value = "status", required = false) List<String> statuses,
        @RequestParam(value = "privacy", required = false) List<String> privacies,

        Pageable pageable,
        PagedResourcesAssembler assembler)
    {
        Specification<Project> projectSpecification =
            Specification.where(ProjectSpecs.getProjectsByMarkerSymbolAndSpecie(markerSymbols)
                .and(ProjectSpecs.getProjectsByWorkUnitNames(workUnits)
                .and(ProjectSpecs.getProjectsByWorkGroup(workGroups)
                .and(ProjectSpecs.getProjectsByPlanType(planTypes)
                .and(ProjectSpecs.getProjectsByStatus(statuses)
                .and(ProjectSpecs.getProjectsByPrivacy(privacies)))))));

        Page<Project> projects = projectService.getProjectsBySpecPro(projectSpecification, pageable);
        Page<Project> filteredProjects = projects.map(p ->
            projectService.getProjectFilteredByPlanAttributes(
                p, workUnits, workGroups, planTypes, statuses, privacies));
        Page<ProjectSummaryDTO> planSummaryDTOPage =
            filteredProjects.map(this::convertToProjectSummaryDTO);

        PagedModel pr =
            assembler.toModel(
                planSummaryDTOPage,
                linkTo(PlanController.class).slash("/projectSummaries").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = {"/projectSummaries/{tpn}"})
    public EntityModel<ProjectSummaryDTO> getProjectSummary(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);
        ProjectSummaryDTO projectSummaryDTO = convertToProjectSummaryDTO(project);

        return new EntityModel<>(projectSummaryDTO);
    }

    private ProjectSummaryDTO convertToProjectSummaryDTO(final Project project)
    {
        ProjectSummaryDTO projectSummaryDTO = projectMapper.projectToProjectSummaryDTO(project);
        List<Plan> plans = planService.getPlansByProject(project);
        List<PlanDetailsDTO> planDetailsDTOs = planMapper.plansToPlanDetailsDTOs(plans);
        projectSummaryDTO.setPlanDetailsDTO(planDetailsDTOs);

        return projectSummaryDTO;
    }
}
