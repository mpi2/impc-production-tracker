package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.guide.Guide;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;
import java.util.Set;

@Component
public class CrisprAttemptMapper
{
    private ModelMapper modelMapper;
    private GuideMapper guideMapper;
    private NucleaseMapper nucleaseMapper;
    private CrisprAttempService crisprAttempService;

    private static final String DELIVERY_TYPE_METHOD_NOT_FOUND = "Delivery Method Type [%s]" +
        " does not exist. Please correct the name or create first the delivery type method.";

    public CrisprAttemptMapper(
        ModelMapper modelMapper,
        GuideMapper guideMapper,
        NucleaseMapper nucleaseMapper, CrisprAttempService crisprAttempService)
    {
        this.modelMapper = modelMapper;
        this.guideMapper = guideMapper;
        this.nucleaseMapper = nucleaseMapper;
        this.crisprAttempService = crisprAttempService;
    }

    public CrisprAttemptDTO toDto(CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = null;
        if (crisprAttempt != null)
        {
            crisprAttemptDTO = modelMapper.map(crisprAttempt, CrisprAttemptDTO.class);
            crisprAttemptDTO.setGuideDTOS(guideMapper.toDtos(crisprAttempt.getGuides()));
        }
        return crisprAttemptDTO;
    }

    public CrisprAttempt toEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = modelMapper.map(crisprAttemptDTO, CrisprAttempt.class);
        setGuidesToEntity(crisprAttempt, crisprAttemptDTO);
        setDeliveryTypeMethodToEntity(crisprAttempt, crisprAttemptDTO);
        return crisprAttempt;
    }

    private void setGuidesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Guide> guides = guideMapper.toEntities(crisprAttemptDTO.getGuideDTOS());
        guides.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setGuides(guides);
    }

    private void setDeliveryTypeMethodToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        String deliveryMethodTypeName = crisprAttemptDTO.getDeliveryTypeMethodName();
        if (deliveryMethodTypeName != null)
        {
            DeliveryMethodType deliveryMethodType =
                crisprAttempService.getDeliveryTypeByName(deliveryMethodTypeName);
            if (deliveryMethodType == null)
            {
                String errorMessage =
                    String.format(DELIVERY_TYPE_METHOD_NOT_FOUND, deliveryMethodTypeName);
                throw new OperationFailedException(errorMessage);
            }
            crisprAttempt.setDeliveryMethodType(deliveryMethodType);
        }
    }
}
