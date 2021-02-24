package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;


@Component
public class PhenotypeAttemptValidator
{
    public PhenotypeAttemptValidator()
    {

    }

    public void validate(PhenotypingAttempt originalAttempt, PhenotypingAttempt phenotypingAttempt)
    {
        // This is a place to add validation code
        // Consider throwing an org.gentar.exceptions.UserOperationFailedException
        // e.g. throw new UserOperationFailedException(errorMessage);
        if ( !originalAttempt.getStrain().equals(phenotypingAttempt.getStrain()) ||
                !originalAttempt.getPhenotypingExternalRef().equals(phenotypingAttempt.getPhenotypingExternalRef())
        )
        {
            if (phenotypingAttempt.getPhenotypingStages() != null) {
                originalAttempt.getPhenotypingStages().forEach(phenotypingStage ->
                {
                    if ( (phenotypingStage.getPhenotypingStageType().getName().equals("early adult and embryo") &&
                            phenotypingStage.getStatus().getOrdering() >= 253000) ||
                            (phenotypingStage.getPhenotypingStageType().getName().equals("late adult") &&
                                    phenotypingStage.getStatus().getOrdering() >= 301000) )
                    {
                        throw new UserOperationFailedException("The external reference and the background strain can " +
                                "not be changed after data has been submitted to the DCC.");
                    }
                });
            }
        }
    }
}
