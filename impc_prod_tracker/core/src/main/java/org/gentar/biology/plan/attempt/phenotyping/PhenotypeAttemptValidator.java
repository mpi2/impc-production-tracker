package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class PhenotypeAttemptValidator
{
    private static final String PHENOTYPING_STAGE_STARTED = "The external reference, the background strain or the " +
            "cohort production work unit can not be changed after data has been submitted to the DCC.";
    private static final String CAN_NOT_BE_NULL = "%s cannot be null.";

    public PhenotypeAttemptValidator() {}

    public void validatePhenotypingExternalRefNotNull(PhenotypingAttempt phenotypingAttempt)
    {
        if (phenotypingAttempt.getPhenotypingExternalRef() == null ||
                phenotypingAttempt.getPhenotypingExternalRef().isEmpty())
        {
            throw new UserOperationFailedException(String.format(CAN_NOT_BE_NULL, "Phenotyping External Reference"));
        }
    }

    public void validateStrainNotNull(PhenotypingAttempt phenotypingAttempt)
    {
        if (phenotypingAttempt.getStrain() == null)
        {
            throw new UserOperationFailedException(String.format(CAN_NOT_BE_NULL, "Background Strain"));
        }
    }

    public void validateDataIfPhenotypeStageExists(PhenotypingAttempt originalAttempt, PhenotypingAttempt phenotypingAttempt)
    {
        // The cohort work unit can be null, so we are going to check this first.
        WorkUnit originalWorkUnit = new WorkUnit();
        if (originalAttempt.getCohortWorkUnit() != null)
        {
            originalWorkUnit = originalAttempt.getCohortWorkUnit();
        } else if (originalAttempt.getPlan().getWorkUnit() != null)
        {
            originalWorkUnit = originalAttempt.getPlan().getWorkUnit();
        }

        WorkUnit newWorkUnit = new WorkUnit();
        if (phenotypingAttempt.getCohortWorkUnit() != null) {
            newWorkUnit = phenotypingAttempt.getCohortWorkUnit();
        } else if (phenotypingAttempt.getPlan().getWorkUnit() != null) {
            newWorkUnit = phenotypingAttempt.getPlan().getWorkUnit();
        }

        if ((!originalWorkUnit.equals(newWorkUnit) ||
                !originalAttempt.getStrain().equals(phenotypingAttempt.getStrain()) ||
                !originalAttempt.getPhenotypingExternalRef().equals(phenotypingAttempt.getPhenotypingExternalRef()))
                && phenotypingAttempt.getPhenotypingStages() != null)
        {
            Set<PhenotypingStage> phenotypingStages = phenotypingAttempt.getPhenotypingStages();
            var matchPhenotypingStage = phenotypingStages.stream().anyMatch(ps -> (ps.getPhenotypingStageType()
                    .getName().equals("early adult and embryo") && ps.getStatus().getOrdering() >= 253000) ||
                    (ps.getPhenotypingStageType().getName().equals("late adult") &&
                            ps.getStatus().getOrdering() >= 301000));

            if (matchPhenotypingStage == true)
            {
                throw new UserOperationFailedException(PHENOTYPING_STAGE_STARTED);
            }
        }
    }
}



