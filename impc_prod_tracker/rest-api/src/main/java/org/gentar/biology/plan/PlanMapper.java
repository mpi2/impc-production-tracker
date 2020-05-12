package org.gentar.biology.plan;

import org.gentar.EntityMapper;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeMapper;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.Mapper;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.project.*;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
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
    private BreedingAttemptMapper breedingAttemptMapper;
    private PhenotypingAttemptMapper phenotypingAttemptMapper;
    private AttemptTypeMapper attemptTypeMapper;
    private FunderMapper funderMapper;
    private WorkUnitMapper workUnitMapper;
    private WorkGroupMapper workGroupMapper;
    private PlanTypeMapper planTypeMapper;
    private ProjectService projectService;
    private TransitionMapper transitionMapper;
    private OutcomeMapper outcomeMapper;
    private PlanService planService;

    private static final String PHENOTYPING_PLAN_WITHOUT_STARTING_POINT_ERROR =
        "The starting point for a phenotyping plan cannot be null.";

    public PlanMapper(
        EntityMapper entityMapper,
        CrisprAttemptMapper crisprAttemptMapper,
        BreedingAttemptMapper breedingAttemptMapper,
        PhenotypingAttemptMapper phenotypingAttemptMapper,
        AttemptTypeMapper attemptTypeMapper,
        FunderMapper funderMapper,
        WorkUnitMapper workUnitMapper,
        WorkGroupMapper workGroupMapper,
        PlanTypeMapper planTypeMapper,
        ProjectService projectService,
        TransitionMapper transitionMapper,
        OutcomeMapper outcomeMapper,
        PlanService planService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.breedingAttemptMapper = breedingAttemptMapper;
        this.phenotypingAttemptMapper = phenotypingAttemptMapper;
        this.attemptTypeMapper = attemptTypeMapper;
        this.funderMapper = funderMapper;
        this.workUnitMapper = workUnitMapper;
        this.workGroupMapper = workGroupMapper;
        this.planTypeMapper = planTypeMapper;
        this.projectService = projectService;
        this.transitionMapper = transitionMapper;
        this.outcomeMapper = outcomeMapper;
        this.planService = planService;
    }

    public PlanDTO toDto(Plan plan)
    {
        PlanDTO planDTO = entityMapper.toTarget(plan, PlanDTO.class);
        addNoMappedData(planDTO, plan);
        return planDTO;
    }

    private void addNoMappedData(PlanDTO planDTO, Plan plan)
    {
        setStartingPointDto(planDTO, plan);
        setAttemptDto(planDTO, plan);
        addStatusStamps(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
        planDTO.setFunderNames(funderMapper.toDtos(plan.getFunders()));
        planDTO.setStatusTransitionDTO(buildStatusTransitionDTO(plan));
    }

    private void setStartingPointDto(PlanDTO planDTO, Plan plan)
    {
        if (plan.getPlanStartingPoints() != null) {
            Set<PlanStartingPoint> planStartingPoints = plan.getPlanStartingPoints();
            if (planStartingPoints.size() == 1) {
                PlanStartingPoint planStartingPoint = planStartingPoints.iterator().next();
                PlanStartingPointDTO planStartingPointDTO = new PlanStartingPointDTO();
                planStartingPointDTO.setId(planStartingPoint.getId());
                planStartingPointDTO.setTpo(planStartingPoint.getOutcome().getTpo());
                planDTO.setPlanStartingPointDTO(planStartingPointDTO);
            }
        }
    }

    private AttemptTypesName getAttemptTypesName(AttemptType attemptType)
    {
        String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();
        return AttemptTypesName.valueOfLabel(attemptTypeName);
    }

    private void setAttemptDto(PlanDTO planDTO, Plan plan)
    {
        AttemptTypesName attemptTypesName = getAttemptTypesName(plan.getAttemptType());
        switch (attemptTypesName)
        {
            case CRISPR:
                setCrisprAttemptDto(planDTO, plan);
                break;
            case HAPLOESSENTIAL_CRISPR:
                //TODO: set the happloessential crispr attempt
                break;
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                setPhenotypingAttemptDto(planDTO, plan);
            break;
            case BREEDING:
                setBreedingAttemptDto(planDTO, plan);
                break;
            default:
        }
    }

    private void setCrisprAttemptDto(PlanDTO planDTO, Plan plan)
    {
        CrisprAttemptDTO crisprAttemptDTO = crisprAttemptMapper.toDto(plan.getCrisprAttempt());
        planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
    }

    private void setPhenotypingAttemptDto(PlanDTO planDTO, Plan plan)
    {
        PhenotypingAttemptDTO phenotypingAttemptDTO =
            phenotypingAttemptMapper.toDto(plan.getPhenotypingAttempt());
        planDTO.setPhenotypingAttemptDTO(phenotypingAttemptDTO);
    }

    private void setBreedingAttemptDto(PlanDTO planDTO, Plan plan)
    {
        BreedingAttemptDTO breedingAttemptDTO =
            breedingAttemptMapper.toDto(plan.getBreedingAttempt());
        planDTO.setBreedingAttemptDTO(breedingAttemptDTO);
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
        setEvent(plan, planDTO);
        plan.setProject(projectService.getProjectByTpn(planDTO.getTpn()));
        Set<Funder> funders = new HashSet<>(funderMapper.toEntities(planDTO.getFunderNames()));
        plan.setFunders(funders);
        plan.setWorkUnit(workUnitMapper.toEntity(planDTO.getWorkUnitName()));
        plan.setWorkGroup(workGroupMapper.toEntity(planDTO.getWorkGroupName()));
        setPlanType(plan, planDTO);
        setAttemptType(plan, planDTO);
        setAttempt(plan, planDTO);
        return plan;
    }

    private void setEvent(Plan plan, PlanDTO planDTO)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO = planDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = planService.getProcessEventByName(plan, action);
        }
        plan.setEvent(processEvent);
    }

    private void setAttempt(Plan plan, PlanDTO planDTO)
    {
        AttemptTypesName attemptTypesName = getAttemptTypesName(plan.getAttemptType());
        switch (attemptTypesName)
        {
            case CRISPR:
                setCrisprAttempt(plan, planDTO);
                break;
            case HAPLOESSENTIAL_CRISPR:
                //TODO: set the happloessential crispr attempt
                break;
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                setPhenotypingAttempt(plan, planDTO);
                setStartingPoint(plan, planDTO);
                break;
            case BREEDING:
                //TODO: set the breeding attempt
                break;
            default:
        }
    }

    private void setCrisprAttempt(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(planDTO.getCrisprAttemptDTO());
            if (plan.getCrisprAttempt().getImitsMiAttempt() != null)
            {
                crisprAttempt.setImitsMiAttempt(plan.getCrisprAttempt().getImitsMiAttempt());
            }
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
    }

    private void setStartingPoint(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getPlanStartingPointDTO() != null)
        {
            PlanStartingPoint planStartingPoint = new PlanStartingPoint();
            planStartingPoint.setPlan(plan);
            Outcome outcome = outcomeMapper.toEntityBytTpo(planDTO.getPlanStartingPointDTO().getTpo());
            planStartingPoint.setOutcome(outcome);

            Set<PlanStartingPoint> planStartingPoints =  new HashSet<>();
            planStartingPoints.add(planStartingPoint);

            plan.setPlanStartingPoints(planStartingPoints);
        }
        else
        {
            throw new UserOperationFailedException(PHENOTYPING_PLAN_WITHOUT_STARTING_POINT_ERROR);
        }
    }

    private void setPhenotypingAttempt(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getPhenotypingAttemptDTO() != null)
        {
            PhenotypingAttempt phenotypingAttempt =
                phenotypingAttemptMapper.toEntity(planDTO.getPhenotypingAttemptDTO());
            phenotypingAttempt.setImitsPhenotypeAttempt(
                plan.getPhenotypingAttempt().getImitsPhenotypeAttempt());
            phenotypingAttempt.setImitsPhenotypingProduction(
                plan.getPhenotypingAttempt().getImitsPhenotypingProduction());
            phenotypingAttempt.setPlan(plan);
            phenotypingAttempt.setId(plan.getId());
            plan.setPhenotypingAttempt(phenotypingAttempt);
        }
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
        }
        else
        {
            plan.setPlanType(planTypeMapper.toEntity(planDTO.getPlanTypeName()));
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
        List<TransitionEvaluation> transitionEvaluations =
            planService.evaluateNextTransitions(plan);
        return transitionMapper.toDtos(transitionEvaluations);
    }
}
