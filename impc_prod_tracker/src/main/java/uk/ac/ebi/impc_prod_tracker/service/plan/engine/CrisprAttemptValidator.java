package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;

/**
 * Class that validates that a Crispr Attempt object is valid
 */
@Component
public class CrisprAttemptValidator
{
    private CrisprAttempService crisprAttempService;

    private static final String DELIVERY_TYPE_METHOD_NOT_FOUND = "Delivery Method Type [%s]" +
        " does not exist. Please correct the name or create first the delivery type method.";

    public CrisprAttemptValidator(CrisprAttempService crisprAttempService)
    {
        this.crisprAttempService = crisprAttempService;
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
            if (crisprAttempService.getDeliveryTypeByName(deliveryMethodType.getName()) == null)
            {
                String errorMessage =
                    String.format(DELIVERY_TYPE_METHOD_NOT_FOUND, deliveryMethodType.getName());
                throw new OperationFailedException(errorMessage);
            }
        }
    }
}
