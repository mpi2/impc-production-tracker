package org.gentar.biology.plan;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.AttemptTypes;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptMapper;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.Mapper;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypes;
import org.gentar.biology.project.*;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionMapper;
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
    private PlanTypeMapper planTypeMapper;
    private ProjectService projectService;
    private PlanStateMachineResolver planStateMachineResolver;
    private TransitionMapper transitionMapper;

    public PlanMapper(
        EntityMapper entityMapper,
        CrisprAttemptMapper crisprAttemptMapper,
        PhenotypingAttemptMapper phenotypingAttemptMapper,
        AttemptTypeMapper attemptTypeMapper,
        FunderMapper funderMapper,
        WorkUnitMapper workUnitMapper,
        WorkGroupMapper workGroupMapper,
        PlanTypeMapper planTypeMapper,
        ProjectService projectService,
        PlanStateMachineResolver planStateMachineResolver,
        TransitionMapper transitionMapper)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.phenotypingAttemptMapper = phenotypingAttemptMapper;
        this.attemptTypeMapper = attemptTypeMapper;
        this.funderMapper = funderMapper;
        this.workUnitMapper = workUnitMapper;
        this.workGroupMapper = workGroupMapper;
        this.planTypeMapper = planTypeMapper;
        this.projectService = projectService;
        this.planStateMachineResolver = planStateMachineResolver;
        this.transitionMapper = transitionMapper;
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
        return plan;
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
        List<ProcessEvent> transitions =
            planStateMachineResolver.getAvailableTransitionsByEntityStatus(plan);
        return transitionMapper.toDtos(transitions);
    }
}
