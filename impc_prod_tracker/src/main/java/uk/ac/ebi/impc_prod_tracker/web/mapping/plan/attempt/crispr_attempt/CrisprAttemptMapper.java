package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.crispr_attempt_reagent.CrisprAttemptReagent;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.guide.Guide;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;

import java.util.HashSet;
import java.util.Set;

@Component
public class CrisprAttemptMapper
{
    private ModelMapper modelMapper;
    private GuideMapper guideMapper;
    private NucleaseMapper nucleaseMapper;
    private MutagenesisDonorMapper mutagenesisDonorMapper;
    private GenotypePrimerMapper genotypePrimerMapper;
    private CrisprAttempService crisprAttempService;

    private static final String DELIVERY_TYPE_METHOD_NOT_FOUND = "Delivery Method Type [%s]" +
        " does not exist. Please correct the name or create first the delivery type method.";

    public CrisprAttemptMapper(
        ModelMapper modelMapper,
        GuideMapper guideMapper,
        NucleaseMapper nucleaseMapper,
        MutagenesisDonorMapper mutagenesisDonorMapper,
        GenotypePrimerMapper genotypePrimerMapper,
        CrisprAttempService crisprAttempService)
    {
        this.modelMapper = modelMapper;
        this.guideMapper = guideMapper;
        this.nucleaseMapper = nucleaseMapper;
        this.mutagenesisDonorMapper = mutagenesisDonorMapper;
        this.genotypePrimerMapper = genotypePrimerMapper;
        this.crisprAttempService = crisprAttempService;

    }

    public CrisprAttemptDTO toDto(CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = null;
        if (crisprAttempt != null)
        {
            crisprAttemptDTO = modelMapper.map(crisprAttempt, CrisprAttemptDTO.class);
            crisprAttemptDTO.setGuideDTOS(guideMapper.toDtos(crisprAttempt.getGuides()));
            crisprAttemptDTO.setGenotypePrimerDTOS(
                genotypePrimerMapper.toDtos(crisprAttempt.getPrimers()));
            crisprAttemptDTO.setMutagenesisDonorDTOS(
                mutagenesisDonorMapper.toDtos(crisprAttempt.getMutagenesisDonors()));
        }
        return crisprAttemptDTO;
    }

    public CrisprAttempt toEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = modelMapper.map(crisprAttemptDTO, CrisprAttempt.class);
        setGuidesToEntity(crisprAttempt, crisprAttemptDTO);
        setDeliveryTypeMethodToEntity(crisprAttempt, crisprAttemptDTO);
        setGenotypePrimersToEntity(crisprAttempt, crisprAttemptDTO);
        setMutagenesisDonorsToEntity(crisprAttempt, crisprAttemptDTO);
        setReagentsToEntity(crisprAttempt, crisprAttemptDTO);
        setNucleasesToEntity(crisprAttempt, crisprAttemptDTO);
        return crisprAttempt;
    }

    private void setGuidesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Guide> guides = guideMapper.toEntities(crisprAttemptDTO.getGuideDTOS());
        guides.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setGuides(guides);
    }

    private void setGenotypePrimersToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<GenotypePrimer> genotypePrimers =
            genotypePrimerMapper.toEntities(crisprAttemptDTO.getGenotypePrimerDTOS());
        genotypePrimers.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setPrimers(genotypePrimers);
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
                throw new UserOperationFailedException(errorMessage);
            }
            crisprAttempt.setDeliveryMethodType(deliveryMethodType);
        }
    }

    private void setMutagenesisDonorsToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<MutagenesisDonor> mutagenesisDonors =
            mutagenesisDonorMapper.toEntities(crisprAttemptDTO.getMutagenesisDonorDTOS());
        mutagenesisDonors.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setMutagenesisDonors(mutagenesisDonors);
    }

    private void setReagentsToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<CrisprAttemptReagent> crisprAttemptReagents = new HashSet<>();
        crisprAttemptReagents.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setCrisprAttemptReagents(crisprAttemptReagents);
    }

    private void setNucleasesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Nuclease> nucleases = new HashSet<>();
        nucleases.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setNucleases(nucleases);
    }
}
