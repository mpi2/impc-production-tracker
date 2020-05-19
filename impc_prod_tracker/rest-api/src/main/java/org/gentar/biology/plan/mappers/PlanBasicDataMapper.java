package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptMapper;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptMapper;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.starting_point.PlanStartingPointMapper;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PlanBasicDataMapper implements Mapper<Plan, PlanBasicDataDTO>
{
    private EntityMapper entity;
    private PlanCommonDataMapper planCommonDataMapper;
    private PlanStartingPointMapper planStartingPointMapper;
    private CrisprAttemptMapper crisprAttemptMapper;
    private BreedingAttemptMapper breedingAttemptMapper;
    private PhenotypingAttemptMapper phenotypingAttemptMapper;

    public PlanBasicDataMapper(
        EntityMapper entity,
        PlanCommonDataMapper planCommonDataMapper,
        PlanStartingPointMapper planStartingPointMapper,
        CrisprAttemptMapper crisprAttemptMapper,
        BreedingAttemptMapper breedingAttemptMapper,
        PhenotypingAttemptMapper phenotypingAttemptMapper)
    {
        this.entity = entity;
        this.planCommonDataMapper = planCommonDataMapper;
        this.planStartingPointMapper = planStartingPointMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.breedingAttemptMapper = breedingAttemptMapper;
        this.phenotypingAttemptMapper = phenotypingAttemptMapper;
    }

    @Override
    public PlanBasicDataDTO toDto(Plan plan)
    {
        PlanCommonDataDTO planCommonDataDTO = planCommonDataMapper.toDto(plan);
        PlanBasicDataDTO planBasicDataDTO = entity.toTarget(plan, PlanBasicDataDTO.class);
        planBasicDataDTO.setPlanCommonDataDTO(planCommonDataDTO);
        setAttemptDto(planBasicDataDTO, plan);
        return planBasicDataDTO;
    }

    private void setAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        assert plan.getAttemptType() != null : "Attempt type is null";
        AttemptTypesName attemptTypesName = getAttemptTypesName(plan.getAttemptType());
        switch (attemptTypesName)
        {
            case CRISPR:
                setCrisprAttemptDto(planBasicDataDTO, plan);
                break;
            case HAPLOESSENTIAL_CRISPR:
                //TODO: set the happloessential crispr attempt
                break;
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                setPhenotypingAttemptDto(planBasicDataDTO, plan);
                setStartingPointToPhenotypingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
                break;
            case BREEDING:
                setBreedingAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToBreedingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
                break;
            default:
        }
    }

    private void setStartingPointToPhenotypingPlanDto(
        PlanBasicDataDTO planBasicDataDTO, Set<PlanStartingPoint> planStartingPoints)
    {
        // Phenotyping plans only have a starting point
        assert planStartingPoints.size() == 1;
        PlanStartingPoint planStartingPoint = planStartingPoints.stream().findFirst().orElse(null);
        PlanStartingPointDTO planStartingPointDTO = planStartingPointMapper.toDto(planStartingPoint);
        planBasicDataDTO.setPlanStartingPointDTO(planStartingPointDTO);
    }

    private void setStartingPointsToBreedingPlanDto(
        PlanBasicDataDTO planBasicDataDTO, Set<PlanStartingPoint> planStartingPoints)
    {
        // A breeding plan for instance can have several starting points
        List<PlanStartingPointDTO> planStartingPointDTOS =
            planStartingPointMapper.toDtos(planStartingPoints);
        planBasicDataDTO.setPlanStartingPointDTOS(planStartingPointDTOS);
    }

    private void setCrisprAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        CrisprAttemptDTO crisprAttemptDTO = crisprAttemptMapper.toDto(plan.getCrisprAttempt());
        planBasicDataDTO.setCrisprAttemptDTO(crisprAttemptDTO);
    }

    private void setPhenotypingAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        PhenotypingAttemptDTO phenotypingAttemptDTO =
            phenotypingAttemptMapper.toDto(plan.getPhenotypingAttempt());
        planBasicDataDTO.setPhenotypingAttemptDTO(phenotypingAttemptDTO);
    }

    private void setBreedingAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        BreedingAttemptDTO breedingAttemptDTO =
            breedingAttemptMapper.toDto(plan.getBreedingAttempt());
        planBasicDataDTO.setBreedingAttemptDTO(breedingAttemptDTO);
    }

    @Override
    public Plan toEntity(PlanBasicDataDTO planBasicDataDTO)
    {
        Plan plan = new Plan();
        {
            if (planBasicDataDTO!= null && planBasicDataDTO.getPlanCommonDataDTO() != null)
            {
                plan = planCommonDataMapper.toEntity(planBasicDataDTO.getPlanCommonDataDTO());
            }
        }
        setAttempt(plan, planBasicDataDTO);
        return plan;
    }


    private void setAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        assert plan.getAttemptType() != null : "Attempt type is null";
        AttemptTypesName attemptTypesName = getAttemptTypesName(plan.getAttemptType());
        switch (attemptTypesName)
        {
            case CRISPR:
                setCrisprAttempt(plan, planBasicDataDTO);
                break;
            case HAPLOESSENTIAL_CRISPR:
                //TODO: set the happloessential crispr attempt
                break;
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                setPhenotypingAttempt(plan, planBasicDataDTO);
                setStartingPoint(plan, planBasicDataDTO);
            break;
            case BREEDING:
                //TODO: set the breeding attempt
                break;
            default:
        }
    }

    private void setCrisprAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt =
                crisprAttemptMapper.toEntity(planBasicDataDTO.getCrisprAttemptDTO());
            if (plan.getCrisprAttempt().getImitsMiAttempt() != null)
            {
                crisprAttempt.setImitsMiAttempt(plan.getCrisprAttempt().getImitsMiAttempt());
            }
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
    }

    private void setStartingPoint(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        PlanStartingPoint planStartingPoint =
            planStartingPointMapper.toEntity(planBasicDataDTO.getPlanStartingPointDTO());
        Set<PlanStartingPoint> planStartingPoints = new HashSet<>();
        planStartingPoints.add(planStartingPoint);
        plan.setPlanStartingPoints(planStartingPoints);
    }

    private void setPhenotypingAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getPhenotypingAttemptDTO() != null)
        {
            PhenotypingAttempt phenotypingAttempt =
                phenotypingAttemptMapper.toEntity(planBasicDataDTO.getPhenotypingAttemptDTO());
            phenotypingAttempt.setImitsPhenotypeAttempt(
                plan.getPhenotypingAttempt().getImitsPhenotypeAttempt());
            phenotypingAttempt.setImitsPhenotypingProduction(
                plan.getPhenotypingAttempt().getImitsPhenotypingProduction());
            phenotypingAttempt.setPlan(plan);
            phenotypingAttempt.setId(plan.getId());
            plan.setPhenotypingAttempt(phenotypingAttempt);
        }
    }

    private AttemptTypesName getAttemptTypesName(AttemptType attemptType)
    {
        String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();
        return AttemptTypesName.valueOfLabel(attemptTypeName);
    }
}
