package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;


@Component
public class PhenotypeAttemptValidator
{
    private static final String PHENOTYPING_STAGE_STARTED = "The external reference, the background strain or the " +
            "cohort production work unit can not be changed after data has been submitted to the DCC.";

    public PhenotypeAttemptValidator()
    {

    }

    public void validate(PhenotypingAttempt originalAttempt, PhenotypingAttempt phenotypingAttempt)
    {
        // This is a place to add validation code
        // Consider throwing an org.gentar.exceptions.UserOperationFailedException
        // e.g. throw new UserOperationFailedException(errorMessage);

        // The cohort work unit can be null, so we are going to check this first.
        WorkUnit originalWorkUnit = originalAttempt.getCohortWorkUnit();
        WorkUnit newWorkUnit = phenotypingAttempt.getCohortWorkUnit();
        if (originalWorkUnit == null)
        {
            originalWorkUnit = new WorkUnit();
        }
        if (newWorkUnit == null)
        {
            newWorkUnit = new WorkUnit();
        }

        if ( !originalWorkUnit.equals(newWorkUnit)  ||
                !originalAttempt.getStrain().equals(phenotypingAttempt.getStrain()) ||
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
                        throw new UserOperationFailedException(PHENOTYPING_STAGE_STARTED);
                    }
                });
            }
        }
    }
}
