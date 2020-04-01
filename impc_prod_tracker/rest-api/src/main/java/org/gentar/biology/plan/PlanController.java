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

import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.engine.events.BreedingPlanEvent;
import org.gentar.biology.plan.engine.events.LateAdultPhenotypePlanEvent;
import org.gentar.biology.plan.engine.events.PhenotypePlanEvent;
import org.gentar.biology.plan.engine.events.ProductionPlanEvent;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.project.*;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.helpers.PaginationHelper;
import org.gentar.helpers.LinkUtil;
import org.gentar.common.history.HistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

    public PlanController(
        HistoryMapper historyMapper,
        PlanService planService,
        PlanMapper planMapper,
        UpdatePlanRequestProcessor updatePlanRequestProcessor)
    {
        this.historyMapper = historyMapper;
        this.planService = planService;
        this.planMapper = planMapper;
        this.updatePlanRequestProcessor = updatePlanRequestProcessor;
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
        System.out.println("Plan created => " + planCreated);

        PlanDTO planCreatedDTO = planMapper.toDto(planCreated);
        System.out.println("Plan Created DTO => " + planCreatedDTO);

        return planCreatedDTO;
    }

    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitNames)
    {
        List<Plan> plans = planService.getPlans(tpns, workUnitNames);

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

    @GetMapping(value = {"/{id}"})
    public PlanDTO findOne(@PathVariable("id") String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        PlanDTO planDTO = planMapper.toDto(plan);

        return planDTO;
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
        PlanType planType = plan.getPlanType();
        AttemptType attemptType = plan.getAttemptType();

        if(PlanTypes.PRODUCTION.getTypeName().equalsIgnoreCase(planType.getName()))
        {
            ProductionPlanEvent productionPlanEvent = getProductionPlanEventFromRequest(planDTO);
            plan.setEvent(productionPlanEvent);
        }
        else if ( (PlanTypes.PHENOTYPING.getTypeName().equalsIgnoreCase(planType.getName()))
                   && (AttemptTypes.EARLY_ADULT.getTypeName().equalsIgnoreCase(attemptType.getName())) )
        {
            PhenotypePlanEvent phenotypePlanEvent = getPhenotypePlanEventFromRequest(planDTO);
            plan.setEvent(phenotypePlanEvent);
        }
        else if ( (PlanTypes.PHENOTYPING.getTypeName().equalsIgnoreCase(planType.getName()))
                   && (AttemptTypes.LATE_ADULT.getTypeName().equalsIgnoreCase(attemptType.getName())) )
        {
            LateAdultPhenotypePlanEvent lateAdultPhenotypePlanEvent = getLateAdultPhenotypePlanEventFromRequest(planDTO);
            plan.setEvent(lateAdultPhenotypePlanEvent);
        }
        else if (PlanTypes.BREEDING.getTypeName().equalsIgnoreCase(planType.getName()))
        {
            BreedingPlanEvent breedingPlanEvent = getBreedingPlanEventFromRequest(planDTO);
            plan.setEvent(breedingPlanEvent);
        }
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

    private ProductionPlanEvent getProductionPlanEventFromRequest(PlanDTO planDTO)
    {
        ProductionPlanEvent productionPlanEvent = null;
        StatusTransitionDTO statusTransitionDTO =  planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            productionPlanEvent = ProductionPlanEvent.getEventByName(action);
        }
        return productionPlanEvent;
    }

    private PhenotypePlanEvent getPhenotypePlanEventFromRequest(PlanDTO planDTO)
    {
        PhenotypePlanEvent phenotypePlanEvent = null;
        StatusTransitionDTO statusTransitionDTO =  planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            phenotypePlanEvent = PhenotypePlanEvent.getEventByName(action);
            System.out.println(">>>>>> >>> >>>> action to execute:::" + action);
        }
        return phenotypePlanEvent;
    }

    private LateAdultPhenotypePlanEvent getLateAdultPhenotypePlanEventFromRequest(PlanDTO planDTO)
    {
        LateAdultPhenotypePlanEvent lateAdultPhenotypePlanEvent = null;
        StatusTransitionDTO statusTransitionDTO =  planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            lateAdultPhenotypePlanEvent = LateAdultPhenotypePlanEvent.getEventByName(action);
            System.out.println(">>>>>> >>> >>>> action to execute:::" + action);
        }
        return lateAdultPhenotypePlanEvent;
    }

    private BreedingPlanEvent getBreedingPlanEventFromRequest(PlanDTO planDTO)
    {
        BreedingPlanEvent breedingPlanEvent = null;
        StatusTransitionDTO statusTransitionDTO =  planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            breedingPlanEvent = BreedingPlanEvent.getEventByName(action);
            System.out.println(">>>>>> >>> >>>> action to execute:::" + action);
        }
        return breedingPlanEvent;
    }
}
