package uk.ac.ebi.impc_prod_tracker.service.plan;

import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.data.biology.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_donor.MutagenesisDonor;

import java.util.Optional;

public interface CrisprAttempService
{
    Optional<CrisprAttempt> getCrisprAttemptByPlanId(Long planId);

    Iterable<GenotypePrimer> getGenotypePrimersByCrisprAttempt(CrisprAttempt crisprAttempt);

    Iterable<Nuclease> getNucleasesByCrisprAttempt(CrisprAttempt crisprAttempt);

    Iterable<MutagenesisDonor> getMutagenesisDonorsByCrisprAttempt(CrisprAttempt crisprAttempt);
}
