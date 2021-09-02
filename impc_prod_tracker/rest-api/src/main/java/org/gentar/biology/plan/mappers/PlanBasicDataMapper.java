package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptMapper;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationAttempt;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationAttemptMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptMapper;
import org.gentar.biology.plan.attempt.esCell.EsCellAttempt;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttemptDTO;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttemptMapper;
import org.gentar.biology.plan.attempt.phenotyping.*;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.starting_point.PlanStartingPointMapper;
import org.gentar.biology.strain.StrainMapper;
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
    private EsCellAttemptMapper esCellAttemptMapper;
    private BreedingAttemptMapper breedingAttemptMapper;
    private CreAlleleModificationAttemptMapper creAlleleModificationAttemptMapper;
    private PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper;
    private PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper;
    private StrainMapper strainMapper;

    public PlanBasicDataMapper(
            PlanCommonDataMapper planCommonDataMapper,
            PlanStartingPointMapper planStartingPointMapper,
            CrisprAttemptMapper crisprAttemptMapper,
            EsCellAttemptMapper esCellAttemptMapper,
            BreedingAttemptMapper breedingAttemptMapper,
            CreAlleleModificationAttemptMapper creAlleleModificationAttemptMapper,
            PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper,
            PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper, StrainMapper strainMapper)
    {
        this.planCommonDataMapper = planCommonDataMapper;
        this.planStartingPointMapper = planStartingPointMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.esCellAttemptMapper = esCellAttemptMapper;
        this.breedingAttemptMapper = breedingAttemptMapper;
        this.creAlleleModificationAttemptMapper = creAlleleModificationAttemptMapper;
        this.phenotypingAttemptCreationMapper = phenotypingAttemptCreationMapper;
        this.phenotypingAttemptResponseMapper = phenotypingAttemptResponseMapper;
        this.strainMapper = strainMapper;
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
            case ES_CELL:
                setEsCellAttemptDto(planBasicDataDTO, plan);
                break;
            case BREEDING:
                setBreedingAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToBreedingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
                break;
            case CRE_ALLELE_MODIFICATION:
                setCreAlleleModificationAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToCreAlleleModificationPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
                break;
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                setPhenotypingAttemptDto(planBasicDataDTO, plan);
                setStartingPointToPhenotypingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
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

    private void setStartingPointsToCreAlleleModificationPlanDto(
            PlanBasicDataDTO planBasicDataDTO, Set<PlanStartingPoint> planStartingPoints)
    {
        // Cre Allele Modification plans only have a starting point
        assert planStartingPoints.size() == 1;
        PlanStartingPoint planStartingPoint = planStartingPoints.stream().findFirst().orElse(null);
        PlanStartingPointDTO planStartingPointDTO = planStartingPointMapper.toDto(planStartingPoint);
        planBasicDataDTO.setModificationPlanStartingPointDTO(planStartingPointDTO);
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

    private void setEsCellAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        EsCellAttemptDTO esCellAttemptDTO = esCellAttemptMapper.toDto(plan.getEsCellAttempt());
        planBasicDataDTO.setEsCellAttemptDTO(esCellAttemptDTO);
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

    private void setCreAlleleModificationAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        CreAlleleModificationAttemptDTO creAlleleModificationAttemptDTO =
                creAlleleModificationAttemptMapper.toDto(plan.getCreAlleleModificationAttempt());
        planBasicDataDTO.setCreAlleleModificationAttemptDTO(creAlleleModificationAttemptDTO);
    }

    @Override
    public Plan toEntity(PlanBasicDataDTO planBasicDataDTO)
    {
        Plan plan = new Plan();
        if (planBasicDataDTO!= null && planBasicDataDTO.getPlanCommonDataDTO() != null)
        {
            plan = planCommonDataMapper.toEntity(planBasicDataDTO.getPlanCommonDataDTO());
            // Set id to plan. If the object is new the id will be null.
            plan.setId(planBasicDataDTO.getId());

            setAttempt(plan, planBasicDataDTO);
        }
        return plan;
    }

    private void setAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCrisprAttemptDTO() != null)
        {
            setCrisprAttempt(plan, planBasicDataDTO);
        }
        else if (planBasicDataDTO.getEsCellAttemptDTO() != null)
        {
            setEsCellAttempt(plan, planBasicDataDTO);
        }
        else if (planBasicDataDTO.getCreAlleleModificationAttemptDTO() != null)
        {
            setCreAlleleModificationAttempt(plan, planBasicDataDTO);
        }
        // Missing an entry here for a Breeding Attempt
        // - but note it could have more than one plan starting point
        else if (planBasicDataDTO.getPhenotypingAttemptCreationDTO() != null)
        {
            setPhenotypingAttempt(plan, planBasicDataDTO);
        }

        if (planBasicDataDTO.getPlanStartingPointDTO() != null)
        {
            setStartingPoint(plan, planBasicDataDTO);
        }
        else if (planBasicDataDTO.getModificationPlanStartingPointDTO() != null)
        {
            setModificationStartingPoint(plan, planBasicDataDTO);
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

    private void setEsCellAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getEsCellAttemptDTO() != null)
        {
            EsCellAttempt esCellAttempt = esCellAttemptMapper.toEntity(planBasicDataDTO.getEsCellAttemptDTO());
            esCellAttempt.setPlan(plan);
            esCellAttempt.setId(plan.getId());
            esCellAttemptMapper.setCassetteTransmission(planBasicDataDTO.getEsCellAttemptDTO(), esCellAttempt);

            plan.setEsCellAttempt(esCellAttempt);
        }
    }

    private void setCreAlleleModificationAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCreAlleleModificationAttemptDTO() != null)
        {
            CreAlleleModificationAttempt creAlleleModificationAttempt =
                    creAlleleModificationAttemptMapper.toEntity(planBasicDataDTO.getCreAlleleModificationAttemptDTO());
            creAlleleModificationAttempt.setDeleterStrain(strainMapper.toEntity(planBasicDataDTO.
                    getCreAlleleModificationAttemptDTO().getDeleterStrainName()));
            creAlleleModificationAttempt.setPlan(plan);
            creAlleleModificationAttempt.setId(plan.getId());
            plan.setCreAlleleModificationAttempt(creAlleleModificationAttempt);
        }
    }

    private void setStartingPoint(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        PlanStartingPoint planStartingPoint =
                planStartingPointMapper.toEntity(planBasicDataDTO.getPlanStartingPointDTO());
        setOneStartingPoint(plan, planStartingPoint);
    }

    private void setModificationStartingPoint(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        PlanStartingPoint planStartingPoint =
                planStartingPointMapper.toEntity(planBasicDataDTO.getModificationPlanStartingPointDTO());
        setOneStartingPoint(plan, planStartingPoint);
    }

    private void setOneStartingPoint(Plan plan, PlanStartingPoint planStartingPoint)
    {
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
