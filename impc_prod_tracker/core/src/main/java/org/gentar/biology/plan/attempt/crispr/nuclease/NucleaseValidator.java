package org.gentar.biology.plan.attempt.crispr.nuclease;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class NucleaseValidator
{
    private static final String NULL_FIELD_ERROR = "[%s] cannot be null.";
    private static final String WRONG_TYPE_ERROR = "[%s] nuclease type doesn't exist.";
    private static final String WRONG_CLASS_ERROR = "[%s] nuclease class doesn't exist.";

    public NucleaseValidator() {

    }

    public void validateNucleaseData(Nuclease nuclease)
    {
        if (nuclease.getNucleaseType() == null)
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Nuclease type"));
        }
        if (nuclease.getNucleaseClass() == null)
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Nuclease class"));
        }
    }
}
