package org.gentar.biology.plan;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.events.BreedingPlanEvent;
import org.gentar.biology.plan.engine.events.LateAdultPhenotypePlanEvent;
import org.gentar.biology.plan.engine.events.PhenotypePlanEvent;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.biology.plan.engine.state.LateAdultPhenotypePlanState;
import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.events.ProductionPlanEvent;
import org.gentar.biology.plan.engine.state.ProductionPlanState;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.Mapper;
import org.gentar.biology.project.PlanTypes;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.status.StatusMapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.crispr_attempt.CrisprAttemptMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PlanMapper implements Mapper<Plan, PlanDTO>
{
    private EntityMapper entityMapper;
    private CrisprAttemptMapper crisprAttemptMapper;
    private AttemptTypeMapper attemptTypeMapper;
    private FunderMapper funderMapper;
    private WorkUnitMapper workUnitMapper;
    private WorkGroupMapper workGroupMapper;
    private StatusMapper statusMapper;
    private PlanTypeMapper planTypeMapper;
    private ProjectService projectService;

    private static final String CRISPR_ATTEMPT_TYPE = "crispr";

    public PlanMapper(
            EntityMapper entityMapper,
            CrisprAttemptMapper crisprAttemptMapper,
            AttemptTypeMapper attemptTypeMapper,
            FunderMapper funderMapper,
            WorkUnitMapper workUnitMapper,
            WorkGroupMapper workGroupMapper, StatusMapper statusMapper,
            PlanTypeMapper planTypeMapper, ProjectService projectService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.attemptTypeMapper = attemptTypeMapper;
        this.funderMapper = funderMapper;
        this.workUnitMapper = workUnitMapper;
        this.workGroupMapper = workGroupMapper;
        this.statusMapper = statusMapper;
        this.planTypeMapper = planTypeMapper;
        this.projectService = projectService;
    }

    public PlanDTO toDto(Plan plan)
    {
        PlanDTO planDTO = entityMapper.toTarget(plan, PlanDTO.class);
        addNoMappedData(planDTO, plan);

        return planDTO;
    }

    private void addNoMappedData(PlanDTO planDTO, Plan plan)
    {
        addAttempt(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
        planDTO.setFunderNames(funderMapper.toDtos(plan.getFunders()));
        planDTO.setStatusTransitionDTO(buildStatusTransitionDTO(plan));
    }

    public List<PlanDTO> toDtos(List<Plan> plans)
    {
        List<PlanDTO> planDTOS = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(plan -> planDTOS.add(toDto(plan)));
        }
        return planDTOS;
    }

    private void addAttempt(PlanDTO planDTO, Plan plan)
    {
        AttemptType attemptType = plan.getAttemptType();
        String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();

        if (CRISPR_ATTEMPT_TYPE.equalsIgnoreCase(attemptTypeName))
        {
            CrisprAttemptDTO crisprAttemptDTO =
                crisprAttemptMapper.toDto(plan.getCrisprAttempt());
            planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
        } else
        {
            //TODO: other attempts
        }

    }

    @Override
    public Plan toEntity(PlanDTO planDTO)
    {
        Plan plan = entityMapper.toTarget(planDTO, Plan.class);
        plan.setProject(projectService.getProjectByTpn(planDTO.getTpn()));
        if (planDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(planDTO.getCrisprAttemptDTO());
            crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
        plan.setPlanType(planTypeMapper.toEntity(planDTO.getPlanTypeName()));
        plan.setAttemptType(attemptTypeMapper.toEntity(planDTO.getAttemptTypeName()));
        Set<Funder> funders = new HashSet<Funder>(funderMapper.toEntities(planDTO.getFunderNames()));
        plan.setFunders(funders);
        plan.setWorkUnit(workUnitMapper.toEntity(planDTO.getWorkUnitName()));
        plan.setWorkGroup(workGroupMapper.toEntity(planDTO.getWorkGroupName()));
        plan.setStatus(statusMapper.toEntity(planDTO.getStatusName()));
        return plan;
    }

    private StatusTransitionDTO buildStatusTransitionDTO(Plan plan)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(plan.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitionsByPlanType(plan));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitionsByPlanType(Plan plan)
    {
        List<TransitionDTO> transitionDTOS = new ArrayList<>();
        String currentStatusName = plan.getStatus().getName();
        PlanType planType = plan.getPlanType();
        if (planType != null){
            if (PlanTypes.PRODUCTION.getTypeName().equalsIgnoreCase(planType.getName()))
            {
                setProductionPlanTransitions(transitionDTOS, currentStatusName);
            }
            else if (PlanTypes.PHENOTYPING.getTypeName().equalsIgnoreCase(planType.getName()))
            {
                setPhenotypePlanTransitions(transitionDTOS, currentStatusName);
            }
            else if (PlanTypes.LATE_ADULT_PHENOTYPING.getTypeName().equalsIgnoreCase(planType.getName()))
            {
                setLateAdultPhenotypePlanTransitions(transitionDTOS, currentStatusName);
            }
            else if (PlanTypes.BREEDING.getTypeName().equalsIgnoreCase(planType.getName()))
            {
                setBreedingPlanTransitions(transitionDTOS, currentStatusName);
            }

        }
        return transitionDTOS;
    }

    private void setProductionPlanTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {
        ProcessState planState = ProductionPlanState.getStateByInternalName(currentStatusName);
        if (planState != null)
        {
            List<ProcessEvent> planEvents =
                    EnumStateHelper.getAvailableEventsByState(ProductionPlanEvent.getAllEvents(), planState);
            planEvents.forEach(x -> {
                TransitionDTO transition = new TransitionDTO();
                transition.setAction(x.getName());
                transition.setDescription(x.getDescription());
                transition.setNextStatus(x.getEndState().getName());
                transition.setNote(x.getTriggerNote());
                transition.setAvailable(x.isTriggeredByUser());
                transitionDTOS.add(transition);
            });
        }
    }

    private void setPhenotypePlanTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {
        ProcessState phenotypePlanState = PhenotypePlanState.getStateByInternalName(currentStatusName);
        if (phenotypePlanState != null)
        {
            List<ProcessEvent> phenotypePlanEvents =
                    EnumStateHelper.getAvailableEventsByState(PhenotypePlanEvent.getAllEvents(), phenotypePlanState);
            phenotypePlanEvents.forEach(x -> {
                TransitionDTO transition = new TransitionDTO();
                transition.setAction(x.getName());
                transition.setDescription(x.getDescription());
                transition.setNextStatus(x.getEndState().getName());
                transition.setNote(x.getTriggerNote());
                transition.setAvailable(x.isTriggeredByUser());
                transitionDTOS.add(transition);
            });
        }
    }

    private void setLateAdultPhenotypePlanTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {
        ProcessState lateAdultPhenotypePlanState = LateAdultPhenotypePlanState.getStateByInternalName(currentStatusName);
        if (lateAdultPhenotypePlanState != null)
        {
            List<ProcessEvent> lateAdultPhenotypePlanEvents =
                    EnumStateHelper.getAvailableEventsByState(LateAdultPhenotypePlanEvent.getAllEvents(), lateAdultPhenotypePlanState);
            lateAdultPhenotypePlanEvents.forEach(x -> {
                TransitionDTO transition = new TransitionDTO();
                transition.setAction(x.getName());
                transition.setDescription(x.getDescription());
                transition.setNextStatus(x.getEndState().getName());
                transition.setNote(x.getTriggerNote());
                transition.setAvailable(x.isTriggeredByUser());
                transitionDTOS.add(transition);
            });
        }
    }

    private void setBreedingPlanTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {
        ProcessState breedingPlanState = BreedingPlanState.getStateByInternalName(currentStatusName);
        if (breedingPlanState != null)
        {
            List<ProcessEvent> breedingPlanEvents =
                    EnumStateHelper.getAvailableEventsByState(BreedingPlanEvent.getAllEvents(), breedingPlanState);
            breedingPlanEvents.forEach(x -> {
                TransitionDTO transition = new TransitionDTO();
                transition.setAction(x.getName());
                transition.setDescription(x.getDescription());
                transition.setNextStatus(x.getEndState().getName());
                transition.setNote(x.getTriggerNote());
                transition.setAvailable(x.isTriggeredByUser());
                transitionDTOS.add(transition);
            });
        }
    }
}
