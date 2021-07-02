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
package org.gentar.biology.plan;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.attempt.crispr.ExonDTO;
import org.gentar.biology.plan.attempt.crispr.WgeMapper;
import org.gentar.biology.plan.filter.PlanFilter;
import org.gentar.biology.plan.filter.PlanFilterBuilder;
import org.gentar.biology.plan.mappers.PlanCreationMapper;
import org.gentar.biology.plan.mappers.PlanResponseMapper;
import org.gentar.biology.project.ProjectService;
import org.gentar.common.history.HistoryDTO;
import org.gentar.helpers.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins="*")
public class PlanController
{
    private final HistoryMapper historyMapper;
    private final PlanService planService;
    private final PlanCreationMapper planCreationMapper;
    private final PlanResponseMapper planResponseMapper;
    private final UpdatePlanRequestProcessor updatePlanRequestProcessor;
    private final ProjectService projectService;
    private final WgeMapper wgeMapper;

    public PlanController(
            HistoryMapper historyMapper,
            PlanService planService,
            PlanCreationMapper planCreationMapper,
            PlanResponseMapper planResponseMapper,
            UpdatePlanRequestProcessor updatePlanRequestProcessor,
            ProjectService projectService,
            WgeMapper wgeMapper)
    {
        this.historyMapper = historyMapper;
        this.planService = planService;
        this.planCreationMapper = planCreationMapper;
        this.planResponseMapper = planResponseMapper;
        this.updatePlanRequestProcessor = updatePlanRequestProcessor;
        this.projectService = projectService;
        this.wgeMapper = wgeMapper;
    }

    /**
     * Creates a new project in the system.
     * @param planCreationDTO Request with data of the plan to be created.
     * @return {@link ChangeResponse} representing the plan created in the system.
     */
    @PostMapping
    public ChangeResponse createPlan(@RequestBody PlanCreationDTO planCreationDTO)
    {
        Plan planToBeCreated = planCreationMapper.toEntity(planCreationDTO);
        projectService.associatePlanToProject(
            planToBeCreated, projectService.getNotNullProjectByTpn(planCreationDTO.getTpn()));
        Plan planCreated = planService.createPlan(planToBeCreated);
        return buildChangeResponse(planCreated);
    }

    /**
     * Get all the plans in the system.
     * @return A collection of {@link PlanResponseDTO} objects.
     */
    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "pin", required = false) List<String> pins,
        @RequestParam(value = "tpn", required = false) List<String> projectTpns,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "statusName", required = false) List<String> statusNames,
        @RequestParam(value = "summaryStatusName", required = false) List<String> summaryStatusNames,
        @RequestParam(value = "typeName", required = false) List<String> typeNames,
        @RequestParam(value = "attemptTypeName", required = false) List<String> attemptTypeNames,
        @RequestParam(value = "imitsMiAttemptId", required = false) List<String> imitsMiAttempts,
        @RequestParam(value = "imitsPhenotypeAttemptId", required = false) List<String> imitsPhenotypeAttempts,
        @RequestParam(value = "phenotypingExternalRef", required = false) List<String> phenotypingExternalRefs,
        @RequestParam(value = "doNotCountTowardsCompleteness", required = false) List<String> doNotCountTowardsCompleteness)
    {
        PlanFilter planFilter = PlanFilterBuilder.getInstance()
            .withTpns(projectTpns)
            .withWorkUnitNames(workUnitNames)
            .withWorkGroupNames(workGroupNames)
            .withStatusNames(statusNames)
            .withSummaryStatusNames(summaryStatusNames)
            .withPins(pins)
            .withPlanTypeNames(typeNames)
            .withAttemptTypeNames(attemptTypeNames)
            .withImitsMiAttemptIds(imitsMiAttempts)
            .withImitsPhenotypeAttemptIds(imitsPhenotypeAttempts)
            .withPhenotypingExternalRefs(phenotypingExternalRefs)
            .withDoNotCountTowardsCompleteness(doNotCountTowardsCompleteness)
            .build();
        Page<Plan> plans = planService.getPageablePlans(pageable, planFilter);
        Page<PlanResponseDTO> planDTOSPage = plans.map(this::getDTO);

        PagedModel pr =
            assembler.toModel(
                planDTOSPage,
                linkTo(methodOn(PlanController.class)
                    .findAll(pageable, assembler, pins, projectTpns, workUnitNames, workGroupNames,
                        statusNames, summaryStatusNames, typeNames, attemptTypeNames,
                        imitsMiAttempts,imitsPhenotypeAttempts, phenotypingExternalRefs, doNotCountTowardsCompleteness
                    )).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private PlanResponseDTO getDTO(Plan plan)
    {
        PlanResponseDTO planResponseDTO = new PlanResponseDTO();
        if (plan != null)
        {
            planResponseDTO = planResponseMapper.toDto(plan);
        }
        return planResponseDTO;
    }

    /**
     * Get a specific plan.
     * @param pin Plan identifier.
     * @return Entity with the plan information.
     */
    @GetMapping(value = {"/{pin}"})
    public EntityModel<?> findOne(@PathVariable String pin)
    {
        EntityModel<PlanResponseDTO> entityModel = null;
        Plan plan = planService.getNotNullPlanByPin(pin);
        PlanResponseDTO planResponseDTO = getDTO(plan);
        entityModel = EntityModel.of(planResponseDTO);
        return entityModel;
    }

    @GetMapping(value = {"/{pin}/history"})
    public List<HistoryDTO> getPlanHistory(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);
        return historyMapper.toDtos(planService.getPlanHistory(plan));
    }

    private Plan getNotNullPlanByPin(String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        return plan;
    }

    @PutMapping(value = {"/{pin}"})
    public ChangeResponse updatePlan(@PathVariable String pin, @RequestBody PlanUpdateDTO planUpdateDTO)
    {
        Plan plan = getPlanToUpdate(pin, planUpdateDTO);
        History history = planService.updatePlan(pin, plan);
        HistoryDTO historyDTO = new HistoryDTO();
        if (history != null)
        {
            historyDTO = historyMapper.toDto(history);
        }
        return buildChangeResponse(plan, Collections.singletonList(historyDTO));
    }

    private Plan getPlanToUpdate(String pin, PlanUpdateDTO planUpdateDTO)
    {
        Plan currentPlan = getNotNullPlanByPin(pin);
        Plan newPlan = new Plan(currentPlan);
        return updatePlanRequestProcessor.getPlanToUpdate(newPlan, planUpdateDTO);
    }

    private ChangeResponse buildChangeResponse(Plan plan)
    {
        List<HistoryDTO> historyList = historyMapper.toDtos(planService.getPlanHistory(plan));
        return buildChangeResponse(plan, historyList);
    }
    private ChangeResponse buildChangeResponse(Plan plan, List<HistoryDTO> historyList)
    {
        ChangeResponse changeResponse = new ChangeResponse();
        changeResponse.setHistoryDTOs(historyList);
        changeResponse.add(linkTo(PlanController.class).slash(plan.getPin()).withSelfRel());
        return changeResponse;
    }

    @GetMapping(value = {"/exons_from_wge/{marker_symbol}"})
    public List<ExonDTO> getExonsInWge (@PathVariable String marker_symbol)
    {
        return wgeMapper.getExonsByMarkerSymbol(capitalize(marker_symbol));
    }
}
