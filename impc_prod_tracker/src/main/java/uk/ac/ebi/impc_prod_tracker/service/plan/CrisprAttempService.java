package uk.ac.ebi.impc_prod_tracker.service.plan;

import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.assay_type.AssayType;

import java.util.List;
import java.util.Optional;

public interface CrisprAttempService
{
    Optional<CrisprAttempt> getCrisprAttemptById(Long planId);

    List<GenotypePrimer> getGenotypePrimersByCrisprAttempt(CrisprAttempt crisprAttempt);

    List<Nuclease> getNucleasesByCrisprAttempt(CrisprAttempt crisprAttempt);

    List<MutagenesisDonor> getMutagenesisDonorsByCrisprAttempt(CrisprAttempt crisprAttempt);

    AssayType getAssayTypeByName(String assayTypeName);

    DeliveryMethodType getDeliveryTypeByName(String deliveryTypeName);
}
