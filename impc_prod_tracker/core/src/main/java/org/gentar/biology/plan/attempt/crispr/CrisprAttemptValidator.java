package org.gentar.biology.plan.attempt.crispr;

import org.springframework.stereotype.Component;

/**
 * Class that validates that a Crispr Attempt object is valid
 */
@Component
public class CrisprAttemptValidator
{
    private CrisprAttemptService crisprAttemptService;

    public CrisprAttemptValidator(CrisprAttemptService crisprAttemptService)
    {
        this.crisprAttemptService = crisprAttemptService;
    }

    public void validate(CrisprAttempt crisprAttempt)
    {
        // This is a place to add validation code
        // Consider throwing an org.gentar.exceptions.UserOperationFailedException
        // e.g. throw new UserOperationFailedException(errorMessage);

        // System.out.println("Validating Crispr Attempt");
    }
}
