package org.gentar.biology.plan.attempt.crispr;

import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.attempt.crispr.guide.GuideValidator;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.plan.attempt.crispr.nuclease.NucleaseValidator;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;


import java.util.Set;

/**
 * Class that validates that a Crispr Attempt object is valid
 */
@Component
public class CrisprAttemptValidator
{
    private CrisprAttemptService crisprAttemptService;
    private final NucleaseValidator nucleaseValidator;
    private final GuideValidator guideValidator;

    private static final String NULL_OBJECT_ERROR = "[%s] cannot be null.";

    public CrisprAttemptValidator(CrisprAttemptService crisprAttemptService,
                                  NucleaseValidator nucleaseValidator,
                                  GuideValidator guideValidator)
    {
        this.crisprAttemptService = crisprAttemptService;
        this.nucleaseValidator = nucleaseValidator;
        this.guideValidator = guideValidator;
    }

    public void validate(CrisprAttempt crisprAttempt)
    {
        // This is a place to add validation code
        // Consider throwing an org.gentar.exceptions.UserOperationFailedException
        // e.g. throw new UserOperationFailedException(errorMessage);
        if (crisprAttempt != null)
        {
            validateNuclease(crisprAttempt);
            validateGuides(crisprAttempt);
        }
    }

    private void validateGuides(CrisprAttempt crisprAttempt)
    {
        Set<Guide> guides = crisprAttempt.getGuides();
        if (guides != null && guides.size() > 0)
        {
            guides.forEach(x -> guideValidator.validateGuideData(x));
        }
        else
        {
            throw new UserOperationFailedException(
                    String.format(NULL_OBJECT_ERROR, "Guides information"));
        }
    }

    private void validateNuclease(CrisprAttempt crisprAttempt)
    {
        Set<Nuclease> nucleases = crisprAttempt.getNucleases();
        if (nucleases != null && nucleases.size() > 0)
        {
            nucleases.forEach(x -> nucleaseValidator.validateNucleaseData(x));
        }
        else
        {
            throw new UserOperationFailedException(
                    String.format(NULL_OBJECT_ERROR, "Nuclease information"));
        }
    }
}
