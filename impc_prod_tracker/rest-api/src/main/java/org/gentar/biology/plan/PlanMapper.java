package org.gentar.biology.plan;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.AttemptTypes;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.events.BreedingPlanEvent;
import org.gentar.biology.plan.engine.events.PhenotypePlanEvent;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.events.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.state.CrisprProductionPlanState;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptMapper;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.Mapper;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypes;
import org.gentar.biology.project.*;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusMapper;
import org.gentar.biology.status.StatusNames;
import org.gentar.biology.status_stamps.StatusStampsDTO;
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
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PlanMapper implements Mapper<Plan, PlanDTO>
{
    private EntityMapper entityMapper;
    private CrisprAttemptMapper crisprAttemptMapper;
    private PhenotypingAttemptMapper phenotypingAttemptMapper;
    private AttemptTypeMapper attemptTypeMapper;
    private FunderMapper funderMapper;
    private WorkUnitMapper workUnitMapper;
    private WorkGroupMapper workGroupMapper;
    private StatusMapper statusMapper;
    private PlanTypeMapper planTypeMapper;
    private ProjectService projectService;

    public PlanMapper(
        EntityMapper entityMapper,
        CrisprAttemptMapper crisprAttemptMapper,
        PhenotypingAttemptMapper phenotypingAttemptMapper,
        AttemptTypeMapper attemptTypeMapper,
        FunderMapper funderMapper,
        WorkUnitMapper workUnitMapper,
        WorkGroupMapper workGroupMapper,
        StatusMapper statusMapper,
        PlanTypeMapper planTypeMapper,
        ProjectService projectService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.phenotypingAttemptMapper = phenotypingAttemptMapper;
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
        addStatusStamps(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
        planDTO.setFunderNames(funderMapper.toDtos(plan.getFunders()));
        planDTO.setStatusTransitionDTO(buildStatusTransitionDTO(plan));
    }

    private void addAttempt(PlanDTO planDTO, Plan plan)
    {
        PlanType planType = plan.getPlanType();
        if (PlanTypes.PRODUCTION.getTypeName().equalsIgnoreCase(planType.getName()))
        {
            AttemptType attemptType = plan.getAttemptType();
            String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();

            if (AttemptTypes.CRISPR.getTypeName().equalsIgnoreCase(attemptTypeName))
            {
                CrisprAttemptDTO crisprAttemptDTO = crisprAttemptMapper.toDto(plan.getCrisprAttempt());
                planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
            } else
            {
                //TODO: other attempts
            }
        }
        else if (PlanTypes.PHENOTYPING.getTypeName().equalsIgnoreCase((planType.getName())))
        {
            planDTO.setPhenotypingAttemptDTO(phenotypingAttemptMapper.toDto(plan.getPhenotypingAttempt()));
        }
    }

    private void addStatusStamps(PlanDTO planDTO, Plan plan)
    {
        Set<PlanStatusStamp> statusStamps = plan.getPlanStatusStamps();
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        if (statusStamps != null)
        {
            statusStamps.forEach(x -> {
                StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
                statusStampsDTO.setStatusName(x.getStatus().getName());
                statusStampsDTO.setDate(x.getDate());
                statusStampsDTOS.add(statusStampsDTO);
            });
        }
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        planDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    @Override
    public Plan toEntity(PlanDTO planDTO)
    {
        Plan plan = entityMapper.toTarget(planDTO, Plan.class);
        plan.setProject(projectService.getProjectByTpn(planDTO.getTpn()));
        Set<Funder> funders = new HashSet<>(funderMapper.toEntities(planDTO.getFunderNames()));
        plan.setFunders(funders);
        plan.setWorkUnit(workUnitMapper.toEntity(planDTO.getWorkUnitName()));
        plan.setWorkGroup(workGroupMapper.toEntity(planDTO.getWorkGroupName()));
        setCrisprAttempt(plan, planDTO);
        setPlanType(plan, planDTO);
        setAttemptType(plan, planDTO);
        setStatusAndSummaryStatus(plan);
        plan.setSummaryStatus(statusMapper.toEntity(StatusNames.PLAN_CREATED));

        return plan;
    }

    private void setStatusAndSummaryStatus(Plan plan)
    {
        Status planCreatedStatus = statusMapper.toEntity(StatusNames.PLAN_CREATED);
        plan.setStatus(planCreatedStatus);
        plan.setSummaryStatus(planCreatedStatus);
    }

    private void setAttemptType(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getAttemptTypeName() != null) {
            plan.setAttemptType(attemptTypeMapper.toEntity(planDTO.getAttemptTypeName()));
        }
    }

    private void setPlanType(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getPlanTypeName() == null)
        {
            plan.setPlanType(planTypeMapper.toEntity("Production"));
        } else {
            plan.setPlanType(planTypeMapper.toEntity(planDTO.getPlanTypeName()));
        }
    }

    private void setCrisprAttempt(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(planDTO.getCrisprAttemptDTO());
            crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
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
            else if (PlanTypes.BREEDING.getTypeName().equalsIgnoreCase(planType.getName()))
            {
                setBreedingPlanTransitions(transitionDTOS, currentStatusName);
            }

        }
        return transitionDTOS;
    }

    private void setProductionPlanTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {
        ProcessState planState = CrisprProductionPlanState.getStateByInternalName(currentStatusName);
        if (planState != null)
        {
            List<ProcessEvent> planEvents =
                    EnumStateHelper.getAvailableEventsByState(CrisprProductionPlanEvent.getAllEvents(), planState);
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
