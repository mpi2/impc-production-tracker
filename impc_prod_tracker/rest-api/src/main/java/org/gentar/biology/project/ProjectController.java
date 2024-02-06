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

package org.gentar.biology.project;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeSummaryDTO;
import org.gentar.biology.outcome.OutcomeSummaryMapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.gentar.biology.plan.mappers.PlanMinimumCreationMapper;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.mappers.ProjectCreationMapper;
import org.gentar.biology.project.mappers.ProjectCsvRecordMapper;
import org.gentar.biology.project.mappers.ProjectResponseMapper;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.common.history.HistoryDTO;
import org.gentar.helpers.CsvWriter;
import org.gentar.helpers.LinkUtil;
import org.gentar.helpers.PlanLinkBuilder;
import org.gentar.helpers.ProjectCsvRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectService projectService;
    private final HistoryMapper historyMapper;
    private final CsvWriter<ProjectCsvRecord> csvWriter;
    private final ProjectCsvRecordMapper projectCsvRecordMapper;
    private final UpdateProjectRequestProcessor updateProjectRequestProcessor;
    private final ProjectCreationMapper projectCreationMapper;
    private final PlanMinimumCreationMapper planMinimumCreationMapper;
    private final ProjectResponseMapper projectResponseMapper;
    private final OutcomeSummaryMapper outcomeSummaryMapper;
    private final ProjectSearchDownloadServiceImpl projectSearchDownloadService;

    ProjectController(
        ProjectService projectService,
        HistoryMapper historyMapper,
        CsvWriter<ProjectCsvRecord> csvWriter,
        ProjectCsvRecordMapper projectCsvRecordMapper,
        UpdateProjectRequestProcessor updateProjectRequestProcessor,
        ProjectCreationMapper projectCreationMapper,
        PlanMinimumCreationMapper planMinimumCreationMapper,
        ProjectResponseMapper projectResponseMapper,
        OutcomeSummaryMapper outcomeSummaryMapper,
        ProjectSearchDownloadServiceImpl projectSearchDownloadService) {
        this.projectService = projectService;
        this.historyMapper = historyMapper;
        this.csvWriter = csvWriter;
        this.projectCsvRecordMapper = projectCsvRecordMapper;
        this.updateProjectRequestProcessor = updateProjectRequestProcessor;
        this.projectCreationMapper = projectCreationMapper;
        this.planMinimumCreationMapper = planMinimumCreationMapper;
        this.projectResponseMapper = projectResponseMapper;
        this.outcomeSummaryMapper = outcomeSummaryMapper;
        this.projectSearchDownloadService = projectSearchDownloadService;
    }

    /**
     * Get all the projects in the system.
     *
     * @return A collection of {@link ProjectResponseDTO} objects.
     */
    @GetMapping
    public ResponseEntity findAll(
        @PageableDefault(size = 40, sort = "tpn", direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
        @RequestParam(value = "gene", required = false) List<String> genes,
        @RequestParam(value = "intention", required = false) List<String> intentions,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiumName", required = false) List<String> consortia,
        @RequestParam(value = "assignmentStatusName", required = false)
        List<String> assignmentNames,
        @RequestParam(value = "summaryStatusName", required = false)
        List<String> summaryStatusNames,
        @RequestParam(value = "privacyName", required = false) List<String> privaciesNames,
        @RequestParam(value = "imitsMiPlanId", required = false) List<String> imitsMiPlans,
        @RequestParam(value = "colonyName", required = false) List<String> colonyNames,
        @RequestParam(value = "phenotypingExternalRef", required = false)
        List<String> phenotypingExternalRefs) {
        ProjectFilter projectFilter = ProjectFilterBuilder.getInstance()
            .withTpns(tpns)
            .withMarkerSymbols(markerSymbols)
            .withIntentions(intentions)
            .withGenes(genes)
            .withAssignments(assignmentNames)
            .withSummaryStatusNames(summaryStatusNames)
            .withPrivacies(privaciesNames)
            .withWorkUnitNames(workUnitNames)
            .withConsortiaNames(consortia)
            .withWorkGroupNames(workGroupNames)
            .withImitsMiPlanId(imitsMiPlans)
            .withProductionColonyName(colonyNames)
            .withPhenotypingExternalRef(phenotypingExternalRefs)
            .build();
        Page<Project> projectsPage = projectService.getProjects(projectFilter, pageable);
        Page<ProjectResponseDTO> projectDtos = projectsPage.map(this::getDTO);
        Link link = linkTo(ProjectController.class).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            assembler.toModel(
                projectDtos,
                link);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    /**
     * Creates a new project in the system.
     *
     * @param projectCreationDTO Request with data of the project to be created.
     * @return {@link ChangeResponse} object with information about the creation.
     */
    @PostMapping
    public ChangeResponse createProject(@RequestBody ProjectCreationDTO projectCreationDTO) {
        Project projectToBeCreated = projectCreationMapper.toEntity(projectCreationDTO);
        Plan planToBeCreated = planMinimumCreationMapper.toEntity(
            projectCreationDTO.getPlanMinimumCreationDTO());
        projectService.associatePlanToProject(planToBeCreated, projectToBeCreated);
        Project createdProject = projectService.createProject(projectToBeCreated);
        return buildChangeResponse(createdProject);
    }

    private ChangeResponse buildChangeResponse(Project project) {
        List<HistoryDTO> historyList =
            historyMapper.toDtos(projectService.getProjectHistory(project));
        return buildChangeResponse(project, historyList);
    }

    private ChangeResponse buildChangeResponse(Project project, List<HistoryDTO> historyList) {
        ChangeResponse changeResponse = new ChangeResponse();
        changeResponse.setHistoryDTOs(historyList);

        Link link = linkTo(ProjectController.class).slash(project.getTpn()).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        changeResponse.add(link);
        return changeResponse;
    }

    @PutMapping(value = {"/{tpn}"})
    public ChangeResponse updateProject(
        @PathVariable String tpn, @RequestBody ProjectUpdateDTO projectUpdateDTO) {
        Project currentProject = projectService.getNotNullProjectByTpn(tpn);
        Project newProject =
            updateProjectRequestProcessor.getProjectToUpdate(currentProject, projectUpdateDTO);
        History history = projectService.updateProject(currentProject, newProject);
        HistoryDTO historyDTO = new HistoryDTO();
        if (history != null) {
            historyDTO = historyMapper.toDto(history);
        }
        return buildChangeResponse(newProject, Collections.singletonList(historyDTO));
    }

    /**
     * Export the projects to a csv file.
     *
     * @param response                Http response.
     * @param tpns                    list of tpns separated by comma.
     * @param markerSymbols           list of marker symbols separated by comma.
     * @param genes                   list of marker symbols/accIds separated by comma.
     * @param intentions              list of intention names separated by comma.
     * @param workUnitNames           list of work units names separated by comma.
     * @param workGroupNames          list of work groups names separated by comma.
     * @param consortia               list of consortia names separated by comma.
     * @param assignmentNames         list of assignment statuses names separated by comma.
     * @param summaryStatusNames      list of summary statuses names separated by comma.
     * @param productionColonyName    List of colony names separated by comma.
     * @param phenotypingExternalRefs List of phenotyping external references separated by comma.
     * @param privaciesNames          list of privacy names separated by comma.
     * @param imitsMiPlans            list of iMits plans separated by comma.
     */
    @GetMapping("/exportProjects")
    public void exportProjects(
        HttpServletResponse response,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
        @RequestParam(value = "gene", required = false) List<String> genes,
        @RequestParam(value = "intention", required = false) List<String> intentions,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiumName", required = false) List<String> consortia,
        @RequestParam(value = "assignmentStatusName", required = false)
        List<String> assignmentNames,
        @RequestParam(value = "summaryStatusName", required = false)
        List<String> summaryStatusNames,
        @RequestParam(value = "privacyName", required = false) List<String> privaciesNames,
        @RequestParam(value = "imitsMiPlanId", required = false) List<String> imitsMiPlans,
        @RequestParam(value = "colonyName", required = false) List<String> productionColonyName,
        @RequestParam(value = "phenotypingExternalRef", required = false)
        List<String> phenotypingExternalRefs)
        throws IOException {
        ProjectFilter projectFilter = ProjectFilterBuilder.getInstance()
            .withTpns(tpns)
            .withMarkerSymbols(markerSymbols)
            .withIntentions(intentions)
            .withGenes(genes)
            .withAssignments(assignmentNames)
            .withSummaryStatusNames(summaryStatusNames)
            .withPrivacies(privaciesNames)
            .withWorkUnitNames(workUnitNames)
            .withConsortiaNames(consortia)
            .withWorkGroupNames(workGroupNames)
            .withImitsMiPlanId(imitsMiPlans)
            .withProductionColonyName(productionColonyName)
            .withPhenotypingExternalRef(phenotypingExternalRefs)
            .build();

        projectSearchDownloadService.writeReport(response, projectFilter);

    }

    private ProjectResponseDTO getDTO(Project project) {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        if (project != null) {
            projectResponseDTO = projectResponseMapper.toDto(project);
            projectResponseDTO.add(
                PlanLinkBuilder.buildPlanLinks(project, PlanTypeName.PRODUCTION,
                    "productionPlans"));
            projectResponseDTO.add(
                PlanLinkBuilder.buildPlanLinks(project, PlanTypeName.PHENOTYPING,
                    "phenotypingPlans"));
        }
        return projectResponseDTO;
    }

    /**
     * Get a specific project.
     *
     * @param tpn tpn Project identifier.
     * @return Entity with the project information.
     */
    @GetMapping(value = {"/{tpn}"})
    public EntityModel<?> findOne(@PathVariable String tpn) {
        EntityModel<ProjectResponseDTO> entityModel;
        Project project = projectService.getNotNullProjectByTpn(tpn);
        ProjectResponseDTO projectResponseDTO = getDTO(project);
        entityModel = EntityModel.of(projectResponseDTO);
        return entityModel;
    }

    @GetMapping(value = {"/{tpn}/history"})
    public List<HistoryDTO> getProjectHistory(@PathVariable String tpn) {
        Project project = projectService.getNotNullProjectByTpn(tpn);

        return historyMapper.toDtos(projectService.getProjectHistory(project));
    }

    @GetMapping(value = {"/{tpn}/productionOutcomesSummaries"})
    public List<OutcomeSummaryDTO> getProductionOutcomesSummaries(@PathVariable String tpn) {
        Project project = projectService.getNotNullProjectByTpn(tpn);
        List<Outcome> productionOutcomes = projectService.getProductionOutcomesByProject(project);
        return outcomeSummaryMapper.toDtos(productionOutcomes);
    }

    @GetMapping(value = {"/{tpn}/firstProductionPlanData"})
    public PlanMinimumCreationDTO getFirstProductionPlanData(@PathVariable String tpn) {
        Project project = projectService.getNotNullProjectByTpn(tpn);
        Plan firstPlan = projectService.getFirstProductionPlan(project);
        return planMinimumCreationMapper.toDto(firstPlan);
    }

    @GetMapping(value = {"/{tpn}/hasConditionalCrisprPlan"})
    public boolean hasConditionalCrisprPlan(@PathVariable String tpn) {
        Project project = projectService.getNotNullProjectByTpn(tpn);
        Set<Plan> plans = project.getPlans();

        if (plans != null) {
            return plans.stream()
                .flatMap(plan -> plan.getOutcomes().stream())
                .flatMap(outcome -> outcome.getMutations().stream())
                .flatMap(mutation -> mutation.getMutationCategorizations().stream())
                .anyMatch(mutationCategorization ->
                    "Conditional".equals(mutationCategorization.getName()) ||
                        "Conditional Ready".equals(mutationCategorization.getName()));
        }

        return false;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
