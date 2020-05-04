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
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypes;
import org.gentar.biology.project.*;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
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
        OutcomeMapper outcomeMapper, PlanService planService)
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
        addStartingPoint(planDTO, plan);
        addAttempt(planDTO, plan);
        addStatusStamps(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
        planDTO.setFunderNames(funderMapper.toDtos(plan.getFunders()));
        planDTO.setStatusTransitionDTO(buildStatusTransitionDTO(plan));
    }

    private void addStartingPoint(PlanDTO planDTO, Plan plan)
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

    private void addAttempt(PlanDTO planDTO, Plan plan)
    {
        PlanType planType = plan.getPlanType();
        if (PlanTypes.PRODUCTION.getTypeName().equals(planType.getName()))
        {
            AttemptType attemptType = plan.getAttemptType();
            String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();

            if (AttemptTypesName.CRISPR.getLabel().equals(attemptTypeName))
            {
                CrisprAttemptDTO crisprAttemptDTO = crisprAttemptMapper.toDto(plan.getCrisprAttempt());
                planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
            }
            else if (AttemptTypesName.BREEDING.getLabel().equals(attemptTypeName))
            {
                BreedingAttemptDTO breedingAttemptDTO =
                    breedingAttemptMapper.toDto(plan.getBreedingAttempt());
                planDTO.setBreedingAttemptDTO(breedingAttemptDTO);
            }
            else
            {
                //TODO: other attempts
            }
        }
        else if (PlanTypes.PHENOTYPING.getTypeName().equals((planType.getName())))
        {
            PhenotypingAttemptDTO phenotypingAttemptDTO = phenotypingAttemptMapper.toDto(plan.getPhenotypingAttempt());
            planDTO.setPhenotypingAttemptDTO(phenotypingAttemptDTO);
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
        setPlanType(plan, planDTO);
        setAttemptType(plan, planDTO);

        PlanType planType = plan.getPlanType();
        if (PlanTypes.PRODUCTION.getTypeName().equalsIgnoreCase(planType.getName()))
        {
            AttemptType attemptType = plan.getAttemptType();
            String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();
            if (AttemptTypesName.CRISPR.getLabel().equalsIgnoreCase(attemptTypeName))
            {
                setCrisprAttempt(plan, planDTO);
            } else
            {
                //TODO: other attempts
            }
        }
        else if (PlanTypes.PHENOTYPING.getTypeName().equalsIgnoreCase((planType.getName())))
        {
            setStartingPoint(plan, planDTO);
//            setStartingPoint(plan, planDTO);
        }

        return plan;
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
        // Phenotyping plans
        if (planDTO.getPlanStartingPointDTO() != null) {
            PlanStartingPoint planStartingPoint = new PlanStartingPoint();
            planStartingPoint.setPlan(plan);
            Outcome outcome = outcomeMapper.toEntityBytTpo(planDTO.getPlanStartingPointDTO().getTpo());
            planStartingPoint.setOutcome(outcome);

            Set<PlanStartingPoint> planStartingPoints =  new HashSet<>();
            planStartingPoints.add(planStartingPoint);

            plan.setPlanStartingPoints(planStartingPoints);
        } else {
            throw new UserOperationFailedException(String.format(PHENOTYPING_PLAN_WITHOUT_STARTING_POINT_ERROR));
        }

        // Breeding plans
//        if (planDTO.getPlanStartingPointDTOS() != null) {
//            // TODO starting points for breeding plans
//        }
    }

    private void setPhenotypingAttempt(Plan plan, PlanDTO planDTO)
    {
        if (planDTO.getPhenotypingAttemptDTO() != null)
        {
            PhenotypingAttempt phenotypingAttempt = phenotypingAttemptMapper.toEntity(planDTO.getPhenotypingAttemptDTO());
            if (plan.getPhenotypingAttempt().getImitsPhenotypeAttempt() != null)
            {
                phenotypingAttempt.setImitsPhenotypeAttempt(plan.getPhenotypingAttempt().getImitsPhenotypeAttempt());
            }
            if (plan.getPhenotypingAttempt().getImitsPhenotypingProduction() != null)
            {
                phenotypingAttempt.setImitsPhenotypingProduction(plan.getPhenotypingAttempt().getImitsPhenotypingProduction());
            }
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
        if (planDTO.getPlanTypeName() == null) {
            plan.setPlanType(planTypeMapper.toEntity("Production"));
        } else {
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
