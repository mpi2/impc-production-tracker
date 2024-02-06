package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptMapper;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttempt;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttemptMapper;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttempt;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttemptMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptMapper;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttempt;
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
    private final PlanCommonDataMapper planCommonDataMapper;
    private final PlanStartingPointMapper planStartingPointMapper;
    private final CrisprAttemptMapper crisprAttemptMapper;
    private final EsCellAttemptMapper esCellAttemptMapper;
    private final BreedingAttemptMapper breedingAttemptMapper;
    private final EsCellAlleleModificationAttemptMapper esCellAlleleModificationAttemptMapper;
    private final CrisprAlleleModificationAttemptMapper crisprAlleleModificationAttemptMapper;
    private final PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper;
    private final PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper;
    private final StrainMapper strainMapper;

    public PlanBasicDataMapper(
            PlanCommonDataMapper planCommonDataMapper,
            PlanStartingPointMapper planStartingPointMapper,
            CrisprAttemptMapper crisprAttemptMapper,
            EsCellAttemptMapper esCellAttemptMapper,
            BreedingAttemptMapper breedingAttemptMapper,
            EsCellAlleleModificationAttemptMapper esCellAlleleModificationAttemptMapper,
            CrisprAlleleModificationAttemptMapper crisprAlleleModificationAttemptMapper,
            PhenotypingAttemptCreationMapper phenotypingAttemptCreationMapper,
            PhenotypingAttemptResponseMapper phenotypingAttemptResponseMapper, StrainMapper strainMapper)
    {
        this.planCommonDataMapper = planCommonDataMapper;
        this.planStartingPointMapper = planStartingPointMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.esCellAttemptMapper = esCellAttemptMapper;
        this.breedingAttemptMapper = breedingAttemptMapper;
        this.esCellAlleleModificationAttemptMapper = esCellAlleleModificationAttemptMapper;
        this.crisprAlleleModificationAttemptMapper = crisprAlleleModificationAttemptMapper;
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
            case CRISPR, HAPLOESSENTIAL_CRISPR->
                setCrisprAttemptDto(planBasicDataDTO, plan);
            case ES_CELL->
                setEsCellAttemptDto(planBasicDataDTO, plan);
            case BREEDING-> {
                setBreedingAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToBreedingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
            }
            case ES_CELL_ALLELE_MODIFICATION->{
                setEsCellAlleleModificationAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToEsCellAlleleModificationPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
            }
            case CRISPR_ALLELE_MODIFICATION-> {
                setCrisprAlleleModificationAttemptDto(planBasicDataDTO, plan);
                setStartingPointsToCrisprAlleleModificationPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
            }
            case ADULT_PHENOTYPING, HAPLOESSENTIAL_PHENOTYPING-> {
                setPhenotypingAttemptDto(planBasicDataDTO, plan);
                setStartingPointToPhenotypingPlanDto(planBasicDataDTO, plan.getPlanStartingPoints());
            }
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

    private void setStartingPointsToEsCellAlleleModificationPlanDto(
            PlanBasicDataDTO planBasicDataDTO, Set<PlanStartingPoint> planStartingPoints)
    {
        // ES Cell Allele Modification plans only have a starting point
        assert planStartingPoints.size() == 1;
        PlanStartingPoint planStartingPoint = planStartingPoints.stream().findFirst().orElse(null);
        PlanStartingPointDTO planStartingPointDTO = planStartingPointMapper.toDto(planStartingPoint);
        planBasicDataDTO.setModificationPlanStartingPointDTO(planStartingPointDTO);
    }

    private void setStartingPointsToCrisprAlleleModificationPlanDto(
        PlanBasicDataDTO planBasicDataDTO, Set<PlanStartingPoint> planStartingPoints)
    {
        // Crispr Allele Modification plans only have a starting point
        assert planStartingPoints.size() == 1;
        PlanStartingPoint planStartingPoint = planStartingPoints.stream().findFirst().orElse(null);
        PlanStartingPointDTO planStartingPointDTO = planStartingPointMapper.toDto(planStartingPoint);
        planBasicDataDTO.setCrisprModificationPlanStartingPointDTO(planStartingPointDTO);
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

    private void setEsCellAlleleModificationAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        EsCellAlleleModificationAttemptDTO esCellAlleleModificationAttemptDTO =
                esCellAlleleModificationAttemptMapper.toDto(plan.getEsCellAlleleModificationAttempt());
        planBasicDataDTO.setEsCellAlleleModificationAttemptDTO(esCellAlleleModificationAttemptDTO);
    }

    private void setCrisprAlleleModificationAttemptDto(PlanBasicDataDTO planBasicDataDTO, Plan plan)
    {
        CrisprAlleleModificationAttemptDTO crisprAlleleModificationAttemptDTO =
            crisprAlleleModificationAttemptMapper.toDto(plan.getCrisprAlleleModificationAttempt());
        planBasicDataDTO.setCrisprAlleleModificationAttemptDTO(crisprAlleleModificationAttemptDTO);
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
        else if (planBasicDataDTO.getEsCellAlleleModificationAttemptDTO() != null)
        {
            setEsCellAlleleModificationAttempt(plan, planBasicDataDTO);
        }
        else if (planBasicDataDTO.getCrisprAlleleModificationAttemptDTO() != null)
        {
            setCrisprAlleleModificationAttempt(plan, planBasicDataDTO);
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
        else if (planBasicDataDTO.getCrisprModificationPlanStartingPointDTO() != null)
        {
            setCrisprModificationStartingPoint(plan, planBasicDataDTO);
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

    private void setEsCellAlleleModificationAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getEsCellAlleleModificationAttemptDTO() != null)
        {
            EsCellAlleleModificationAttempt esCellAlleleModificationAttempt =
                    esCellAlleleModificationAttemptMapper.toEntity(planBasicDataDTO.getEsCellAlleleModificationAttemptDTO());
            esCellAlleleModificationAttempt.setDeleterStrain(strainMapper.toEntity(planBasicDataDTO.
                    getEsCellAlleleModificationAttemptDTO().getDeleterStrainName()));
            esCellAlleleModificationAttempt.setPlan(plan);
            esCellAlleleModificationAttempt.setId(plan.getId());
            plan.setEsCellAlleleModificationAttempt(esCellAlleleModificationAttempt);
        }
    }

    private void setCrisprAlleleModificationAttempt(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        if (planBasicDataDTO.getCrisprAlleleModificationAttemptDTO() != null)
        {
            CrisprAlleleModificationAttempt crisprAlleleModificationAttempt =
                crisprAlleleModificationAttemptMapper.toEntity(planBasicDataDTO.getCrisprAlleleModificationAttemptDTO());
            crisprAlleleModificationAttempt.setDeleterStrain(strainMapper.toEntity(planBasicDataDTO.getCrisprAlleleModificationAttemptDTO().getDeleterStrainName()));
            crisprAlleleModificationAttempt.setPlan(plan);
            crisprAlleleModificationAttempt.setId(plan.getId());
            plan.setCrisprAlleleModificationAttempt(crisprAlleleModificationAttempt);
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

    private void setCrisprModificationStartingPoint(Plan plan, PlanBasicDataDTO planBasicDataDTO)
    {
        PlanStartingPoint planStartingPoint =
            planStartingPointMapper.toEntity(planBasicDataDTO.getCrisprModificationPlanStartingPointDTO());
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
