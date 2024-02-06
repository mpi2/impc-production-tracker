package org.gentar.biology.colony;

import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.plan.starting_point.PlanStartingPointRepository;
import org.gentar.biology.status.StatusNames;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColonyValidator
{
    private static final String NULL_FIELD_ERROR = "[%s] cannot be null.";
    private static final String COLONY_NAME_ALREADY_EXISTS_ERROR = "The colony [%s] already exists.";
    private static final String COLONY_IS_A_STARTING_POINT = "The name and/or the background strain can not be " +
            "change because the colony has active phenotyping data associated with it.";
    private static final String COLONY_IS_NOT_GENOTYPE_CONFIRMED = "The colony needs to be genotype confirmed in order " +
            "to create a phenotyping plan.";

    private final ColonyRepository colonyRepository;
    private final PlanStartingPointRepository planStartingPointRepository;

    public ColonyValidator(ColonyRepository colonyRepository,
                           PlanStartingPointRepository planStartingPointRepository)
    {
        this.colonyRepository = colonyRepository;
        this.planStartingPointRepository = planStartingPointRepository;
    }

    public void validateData(Colony colony)
    {
        if (Strings.isBlank(colony.getName()))
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Colony name"));
        }
        if (colony.getStrain() == null)
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Background Strain"));
        }
        validateNewColonyNameDoesNotExist(colony);
    }

    public void validateDataForStartingPoint(Colony colony)
    {
        if (!StatusNames.GENOTYPE_CONFIRMED.equals(colony.getProcessDataStatus().getName()))
        {
            throw new UserOperationFailedException(COLONY_IS_NOT_GENOTYPE_CONFIRMED);
        }
    }

    public void validateUpdateData(Colony originalColony, Colony colony)
    {
        validateData(colony);
        validateNewColonyDoesIsNotAStartingPoint(originalColony, colony);
    }

    private void validateNewColonyDoesIsNotAStartingPoint(Colony originalColony, Colony colony)
    {
        if (!originalColony.getName().equals(colony.getName()) ||
                !originalColony.getStrain().equals(colony.getStrain()))
        {
            List<PlanStartingPoint> planStartingPoints = planStartingPointRepository.findByOutcomeId(colony.getOutcome()
                    .getId());
            if (planStartingPoints != null) {
                planStartingPoints.forEach(planStartingPoint -> {

                    PhenotypingAttempt phenotypingAttempt =planStartingPoint.getPlan().getPhenotypingAttempt();
                    if(phenotypingAttempt != null){
                    phenotypingAttempt.getPhenotypingStages().forEach(phenotypingStage ->
                    {
                        if ( (phenotypingStage.getPhenotypingStageType().getName().equals("early adult and embryo") &&
                                phenotypingStage.getProcessDataStatus().getOrdering() >= 253000) ||
                                (phenotypingStage.getPhenotypingStageType().getName().equals("late adult") &&
                                        phenotypingStage.getProcessDataStatus().getOrdering() >= 301000) )
                        {
                            throw new UserOperationFailedException(COLONY_IS_A_STARTING_POINT);
                        }
                    });
                    }
                });
            }
        }
    }

    private void validateNewColonyNameDoesNotExist(Colony colony)
    {
        if (colony.getId() == null)
        {
            String colonyName = colony.getName();
            Colony existingColony = colonyRepository.findByName(colonyName);
            if (existingColony != null)
            {
                throw new UserOperationFailedException(String.format(
                    COLONY_NAME_ALREADY_EXISTS_ERROR, colonyName));
            }
        }
    }
}
