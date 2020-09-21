package org.gentar.biology.plan.mappers;

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
import org.gentar.biology.plan.attempt.phenotyping.*;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.starting_point.PlanStartingPointMapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PlanBasicDataMapper implements Mapper<Plan, PlanBasicDataDTO>
{
    private PlanCommonDataMapper planCommonDataMapper;
    private PlanStartingPointMapper planStartingPointMapper;
    private CrisprAttemptMapper crisprAttemptMapper;
    private BreedingAttemptMapper breedingAttemptMapper;
    private PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper;
    private PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper;

    public PlanBasicDataMapper(
            PlanCommonDataMapper planCommonDataMapper,
            PlanStartingPointMapper planStartingPointMapper,
            CrisprAttemptMapper crisprAttemptMapper,
            BreedingAttemptMapper breedingAttemptMapper,
            PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper,
            PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper)
    {
        this.planCommonDataMapper = planCommonDataMapper;
        this.planStartingPointMapper = planStartingPointMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.breedingAttemptMapper = breedingAttemptMapper;
        this.phenotypingAttemptCreationMapper = phenotypingAttemptCreationMapper;
        this.phenotypingAttemptResponseMapper = phenotypingAttemptResponseMapper;
    }

    @Override
    public PlanBasicDataDTO toDto(Plan plan)
    {
        PlanCommonDataDTO planCommonDataDTO = planCommonDataMapper.toDto(plan);
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(planCommonDataDTO);
        setAttemptDto(planBasicDataDTO, plan);
        return planBasicDataDTO;
    }

    private void setAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        if (plan.getAttemptType() == null)
        {
            throw new UserOperationFailedException("Attempt type is null");
        }
        AttemptTypesName attemptTypesName = getAttemptTypesName(plan.getAttemptType());
        switch (attemptTypesName)
        {
            case CRISPR: case HAPLOESSENTIAL_CRISPR:
                setCrisprAttemptDto(planBasicDataDTO, plan);
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
        PhenotypingAttemptResponseDTO phenotypingAttemptResponseDTO =
            phenotypingAttemptResponseMapper.toDto(plan.getPhenotypingAttempt());
        planBasicDataDTO.setPhenotypingAttemptResponseDTO(phenotypingAttemptResponseDTO);
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
                // Set id to plan. If the object is new the id will be null.
                plan.setId(planBasicDataDTO.getId());

                setAttempt(plan, planBasicDataDTO);
            }
        }
        return plan;
    }

    private void setAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCrisprAttemptDTO() != null)
        {
            setCrisprAttempt(plan, planBasicDataDTO);
        }
        else if (planBasicDataDTO.getPhenotypingAttemptCreationDTO() != null)
        {
            setPhenotypingAttempt(plan, planBasicDataDTO);
        }
        if (planBasicDataDTO.getPlanStartingPointDTO() != null)
        {
            setStartingPoint(plan, planBasicDataDTO);
        }
    }

    private void setCrisprAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt =
                crisprAttemptMapper.toEntity(planBasicDataDTO.getCrisprAttemptDTO());
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
    }

    private void setStartingPoint(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        PlanStartingPoint planStartingPoint =
            planStartingPointMapper.toEntity(planBasicDataDTO.getPlanStartingPointDTO());
        planStartingPoint.setPlan(plan);
        Set<PlanStartingPoint> planStartingPoints = new HashSet<>();
        planStartingPoints.add(planStartingPoint);
        plan.setPlanStartingPoints(planStartingPoints);
    }

    private void setPhenotypingAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getPhenotypingAttemptCreationDTO() != null)
        {
            PhenotypingAttempt phenotypingAttempt =
                phenotypingAttemptCreationMapper.toEntity(planBasicDataDTO.getPhenotypingAttemptCreationDTO());
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
