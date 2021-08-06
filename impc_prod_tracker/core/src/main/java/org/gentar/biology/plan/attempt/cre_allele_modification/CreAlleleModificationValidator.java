package org.gentar.biology.plan.attempt.cre_allele_modification;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * Class that validates that a Cre Allele Modification Attempt object is valid
 */
@Component
public class CreAlleleModificationValidator
{
    private static final String CAN_NOT_BE_NULL = "%s cannot be null.";

    public CreAlleleModificationValidator () { }

    public void validateModificationExternalRefNotNull(CreAlleleModificationAttempt creAlleleModificationAttempt)
    {
        if (creAlleleModificationAttempt.getModificationExternalRef() == null ||
                creAlleleModificationAttempt.getModificationExternalRef().isEmpty())
        {
            throw new UserOperationFailedException(String.format(CAN_NOT_BE_NULL, "Modification External Reference"));
        }
    }
}
