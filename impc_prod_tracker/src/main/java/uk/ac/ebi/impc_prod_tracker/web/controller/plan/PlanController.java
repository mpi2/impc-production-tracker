/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.web.controller.plan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.web.controller.util.LinkUtil;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanMapper;
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

    public PlanController(HistoryMapper historyMapper, PlanService planService, PlanMapper planMapper)
    {
        this.historyMapper = historyMapper;
        this.planService = planService;
        this.planMapper = planMapper;
    }

    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "work_unit_name", required = false) List<String> workUnitNames)
    {
        Page<Plan> plansPage = planService.getPlans(pageable, tpns, workUnitNames);
        Page<PlanDTO> planDTOPage = plansPage.map(this::getDTO);
        PagedModel pr =
            assembler.toModel(
                planDTOPage,
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

    @PutMapping(value = {"/{pin}"})
    public HistoryDTO updatePlan(
        @PathVariable String pin, @RequestBody PlanDTO planDTO)
    {
        History history = planService.updatePlan(pin, planDTO);
        if (history != null)
        {
            return historyMapper.toDto(history);
        }
        return null;
    }
}
