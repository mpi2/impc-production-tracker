package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhenotypingStageValidator
{
    private final PhenotypingStageRepository phenotypingStageRepository;

    public PhenotypingStageValidator(PhenotypingStageRepository phenotypingStageRepository)
    {
        this.phenotypingStageRepository = phenotypingStageRepository;
    }

    /**
     * Validates that a given phenotypingStage has valid data to be created/updated.
     * @param phenotypingStage The PhenotypingStage object.
     */
    public void validate(PhenotypingStage phenotypingStage)
    {
        PhenotypingAttempt phenotypingAttempt = phenotypingStage.getPhenotypingAttempt();
        if (phenotypingAttempt == null)
        {
            throw new UserOperationFailedException(
                "The phenotyping stage requires a phenotyping attempt.");
        }
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();
        if (phenotypingStageType == null)
        {
            throw new UserOperationFailedException(
                "The phenotyping stage requires a phenotyping stage type.");
        }
        List<PhenotypingStage> existingByType =
            phenotypingStageRepository.findAllByPhenotypingAttemptAndPhenotypingStageType(
                phenotypingAttempt, phenotypingStageType);
        if (existingByType != null && !existingByType.isEmpty())
        {
            throw new UserOperationFailedException(
                "A phenotyping stage of type [" + phenotypingStageType.getName() +
                    "] already exists for the plan " + phenotypingAttempt.getPlan().getPin() + ".");
        }
    }
}
