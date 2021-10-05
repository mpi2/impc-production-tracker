package org.gentar.biology.plan.attempt.es_cell_allele_modification;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * Class that validates that a Es Cell Allele Modification Attempt object is valid
 */
@Component
public class EsCellAlleleModificationValidator
{
    private static final String CAN_NOT_BE_NULL = "%s cannot be null.";

    public EsCellAlleleModificationValidator() { }

    public void validateModificationExternalRefNotNull(EsCellAlleleModificationAttempt esCellAlleleModificationAttempt)
    {
        if (esCellAlleleModificationAttempt.getModificationExternalRef() == null ||
                esCellAlleleModificationAttempt.getModificationExternalRef().isEmpty())
        {
            throw new UserOperationFailedException(String.format(CAN_NOT_BE_NULL, "Modification External Reference"));
        }
    }
}
