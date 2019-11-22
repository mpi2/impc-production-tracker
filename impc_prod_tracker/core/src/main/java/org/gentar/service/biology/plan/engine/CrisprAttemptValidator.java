package org.gentar.service.biology.plan.engine;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.service.biology.plan.crispr.CrisprAttemptService;
import org.springframework.stereotype.Component;
import org.gentar.biology.crispr_attempt.CrisprAttempt;
import org.gentar.biology.crispr_attempt.delivery_type.DeliveryMethodType;

/**
 * Class that validates that a Crispr Attempt object is valid
 */
@Component
public class CrisprAttemptValidator
{
    private CrisprAttemptService crisprAttemptService;

    private static final String DELIVERY_TYPE_METHOD_NOT_FOUND = "Delivery Method Type [%s]" +
        " does not exist. Please correct the name or create first the delivery type method.";

    public CrisprAttemptValidator(CrisprAttemptService crisprAttemptService)
    {
        this.crisprAttemptService = crisprAttemptService;
    }

    void validate(CrisprAttempt crisprAttempt)
    {
        System.out.println("Validating Crispr Attempt");
        validateDeliverTypeMethodExists(crisprAttempt.getDeliveryMethodType());
    }

    private void validateDeliverTypeMethodExists(DeliveryMethodType deliveryMethodType)
    {
        if (deliveryMethodType != null)
        {
            if (crisprAttemptService.getDeliveryTypeByName(deliveryMethodType.getName()) == null)
            {
                String errorMessage =
                    String.format(DELIVERY_TYPE_METHOD_NOT_FOUND, deliveryMethodType.getName());
                throw new UserOperationFailedException(errorMessage);
            }
        }
    }
}
