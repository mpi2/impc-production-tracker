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
package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.controller.project.ProjectDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.controller.project.ProjectDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.ProjectPlanDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PlanController
{
    private PlanDTOBuilder planDTOBuilder;
    private ProjectDTOBuilder projectDTOBuilder;

    public PlanController(PlanDTOBuilder planDTOBuilder, ProjectDTOBuilder projectDTOBuilder)
    {
        this.planDTOBuilder = planDTOBuilder;
        this.projectDTOBuilder = projectDTOBuilder;
    }

    @GetMapping(value = {"/plans"})
    public List<ProjectPlanDTO> getPlans()
    {
        List<Plan> plans = projectDTOBuilder.getPlanService().getPlans();

        List<ProjectPlanDTO> projectPlanDTOS = new ArrayList<>();
        for (Plan plan : plans)
        {
            ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
            PlanDTO planDTO = planDTOBuilder.buildPlanDTOFromPlan(plan);

            projectPlanDTO.setPlanDTO(planDTO);
            projectPlanDTO.setProjectDetailsDTO(
                projectDTOBuilder.buildProjectDetailsDTOFromProject(plan.getProject()));
            projectPlanDTOS.add(projectPlanDTO);
        }

        return projectPlanDTOS;
    }

    @GetMapping(value = {"/plans/{pin}"})
    public ProjectPlanDTO getPlan(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        ProjectDetailsDTO projectDetailsDTO =
            projectDTOBuilder.buildProjectDetailsDTOFromProject(plan.getProject());
        ProjectPlanDTO projectPlanDTO =  new ProjectPlanDTO();

        projectPlanDTO.setProjectDetailsDTO(projectDetailsDTO);
        projectPlanDTO.setPlanDTO(planDTOBuilder.buildPlanDTOFromPlan(plan));

        return projectPlanDTO;
    }

    @GetMapping(value = {"/planSummaries"})
    public ResponseEntity<PagedResources<PlanSummaryDTO>> getPlanSummariesPaginated(
        Pageable pageable, PagedResourcesAssembler assembler)
    {
        Page<Plan> plans = projectDTOBuilder.getPlanService().getPaginatedPlans(pageable);
        Page<PlanSummaryDTO> planSummaryDTOPage = plans.map(this::convertToPlanSummaryDTO);
        PagedResources pr =
            assembler.toResource(
                planSummaryDTOPage,
                linkTo(PlanController.class).slash("/planSummaries").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link",createLinkHeader(pr));

        return new ResponseEntity<>(pr,responseHeaders,HttpStatus.OK);

    }

    @GetMapping(value = {"/planSummaries/{pin}"})
    public PlanSummaryDTO getPlanSummary(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);
        PlanSummaryDTO planSummaryDTO = convertToPlanSummaryDTO(plan);

        return planSummaryDTO;
    }

    private Plan getNotNullPlanByPin(String pin)
    {
        Plan plan = projectDTOBuilder.getPlanService().getPlanByPin(pin);
        if (plan == null)
        {
            throw new OperationFailedException(
                String.format("Plan %s does not exist.", pin), HttpStatus.NOT_FOUND);
        }
        return plan;
    }


    private String createLinkHeader(PagedResources<PlanSummaryDTO> pr){
        final StringBuilder linkHeader = new StringBuilder();
        if (!pr.getLinks("first").isEmpty())
        {
            linkHeader.append(buildLinkHeader( pr.getLinks("first").get(0).getHref(),"first"));
            linkHeader.append(", ");
        }
        if (!pr.getLinks("next").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(),"next"));
        }
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    private PlanSummaryDTO convertToPlanSummaryDTO(final Plan plan)
    {
        PlanSummaryDTO planSummaryDTO = new PlanSummaryDTO();

        PlanDetailsDTO planDetailsDTO = planDTOBuilder.buildPlanDetailsDTOFromPlan(plan);

        planSummaryDTO.setPlanDetailsDTO(planDetailsDTO);
        planSummaryDTO.setProjectDetailsDTO(
            projectDTOBuilder.buildProjectDetailsDTOFromProject(plan.getProject()));
        return planSummaryDTO;
    }
}
