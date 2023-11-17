package org.gentar.biology.plan.attempt.crispr_allele_modification;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * Class that validates that a Crispr Allele Modification Attempt object is valid
 */
@Component
public class CrisprAlleleModificationValidator
{
    private static final String CAN_NOT_BE_NULL = "%s cannot be null.";

    public CrisprAlleleModificationValidator() { }

    public void validateModificationExternalRefNotNull(
        CrisprAlleleModificationAttempt crisprModificationAttempt)
    {
        if (crisprModificationAttempt.getModificationExternalRef() == null ||
            crisprModificationAttempt.getModificationExternalRef().isEmpty())
        {
            throw new UserOperationFailedException(String.format(CAN_NOT_BE_NULL, "Modification External Reference"));
        }
    }
}
