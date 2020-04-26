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

import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.helpers.PaginationHelper;
import org.gentar.helpers.LinkUtil;
import org.gentar.common.history.HistoryDTO;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins="*")
public class PlanController
{
    private HistoryMapper historyMapper;
    private PlanService planService;
    private PlanMapper planMapper;
    private UpdatePlanRequestProcessor updatePlanRequestProcessor;
    private PlanStateMachineResolver planStateMachineResolver;

    private static final String PLAN_NOT_EXISTS_ERROR =
            "The plan[%s] does not exist.";

    public PlanController(
        HistoryMapper historyMapper,
        PlanService planService,
        PlanMapper planMapper,
        UpdatePlanRequestProcessor updatePlanRequestProcessor,
        PlanStateMachineResolver planStateMachineResolver)
    {
        this.historyMapper = historyMapper;
        this.planService = planService;
        this.planMapper = planMapper;
        this.updatePlanRequestProcessor = updatePlanRequestProcessor;
        this.planStateMachineResolver = planStateMachineResolver;
    }

    /**
     * Creates a new project in the system.
     * @param planDTO Request with data of the project to be created.
     * @return {@link PlanDTO} representing the project created in the system.
     */
    @PostMapping
    public PlanDTO createPlan(@RequestBody PlanDTO planDTO)
    {
        Plan planToBeCreated = planMapper.toEntity(planDTO);
        Plan planCreated = planService.createPlan(planToBeCreated);
        PlanDTO planCreatedDTO = planMapper.toDto(planCreated);

        return planCreatedDTO;
    }

    /**
     * Get all the plans in the system.
     * @return A collection of {@link PlanDTO} objects.
     */
    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "projectTpns", required = false) List<String> projectTpns,
        @RequestParam(value = "workUnitNames", required = false) List<String> workUnitNames,
        @RequestParam(value = "workGroupNames", required = false) List<String> workGroupNames,
        @RequestParam(value = "summaryStatusNames", required = false) List<String> summaryStatusNames,
        @RequestParam(value = "pins", required = false) List<String> pins,
        @RequestParam(value = "typeNames", required = false) List<String> typeNames,
        @RequestParam(value = "attemptTypeNames", required = false) List<String> attemptTypeNames,
        @RequestParam(value = "imitsMiAttemptIds", required = false) List<String> imitsMiAttempts,
        @RequestParam(value = "imitsPhenotypeAttemptIds", required = false) List<String> imitsPhenotypeAttempts)
    {
        List<Plan> plans = planService.getPlans(projectTpns, workUnitNames, workGroupNames, summaryStatusNames, pins, typeNames, attemptTypeNames, imitsMiAttempts, imitsPhenotypeAttempts);

        Page<Plan> paginatedContent =
            PaginationHelper.createPage(plans, pageable);
        Page<PlanDTO> planDTOSPage = paginatedContent.map(this::getDTO);

        PagedModel pr =
            assembler.toModel(
                planDTOSPage,
                linkTo(PlanController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private PlanDTO getDTO(Plan plan)
    {
        PlanDTO planDTO = new PlanDTO();
        if (plan != null)
        {
            planDTO = planMapper.toDto(plan);
        }
        return planDTO;
    }

    /**
     * Get a specific plan.
     * @param pin Plan identifier.
     * @return Entity with the plan information.
     */
    @GetMapping(value = {"/{pin}"})
    public EntityModel<?> findOne(@PathVariable String pin)
    {
        EntityModel<PlanDTO> entityModel;
        Plan plan = planService.getNotNullPlanByPin(pin);
        PlanDTO planDTO = getDTO(plan);

        if (planDTO != null)
        {
            entityModel = new EntityModel<>(planDTO);
        }
        else
        {
            //TODO: Exception not found
            throw new UserOperationFailedException(String.format(PLAN_NOT_EXISTS_ERROR, pin));
        }

        return entityModel;
    }

    @GetMapping(value = {"{pin}/history"})
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

    private void setEventByPlanType(Plan plan, PlanDTO planDTO)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO =  planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = planStateMachineResolver.getProcessEventByActionName(plan, action);
        }
        plan.setEvent(processEvent);
    }

    @PutMapping(value = {"/{pin}"})
    public HistoryDTO updatePlan(
        @PathVariable String pin, @RequestBody PlanDTO planDTO)
    {
        Plan plan = getPlanToUpdate(pin, planDTO);
        setEventByPlanType(plan, planDTO);
        History history = planService.updatePlan(pin, plan);
        HistoryDTO historyDTO = new HistoryDTO();
        if (history != null)
        {
            historyDTO = historyMapper.toDto(history);
        }
        return historyDTO;
    }

    private Plan getPlanToUpdate(String pin, PlanDTO planDTO)
    {
        Plan currentPlan = getNotNullPlanByPin(pin);
        Plan newPlan = new Plan(currentPlan);
        return updatePlanRequestProcessor.getPlanToUpdate(newPlan, planDTO);
    }
}
