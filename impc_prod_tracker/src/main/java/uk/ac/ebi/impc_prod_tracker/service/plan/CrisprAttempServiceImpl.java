package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttemptRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.NucleaseRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.genotype_primer.GenotypePrimerRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_donor.MutagenesisDonorRepository;
import java.util.List;
import java.util.Optional;

@Component
public class CrisprAttempServiceImpl implements CrisprAttempService
{
    private CrisprAttemptRepository crisprAttemptRepository;
    private GenotypePrimerRepository genotypePrimerRepository;
    private NucleaseRepository nucleaseRepository;
    private MutagenesisDonorRepository mutagenesisDonorRepository;

    public CrisprAttempServiceImpl(
        CrisprAttemptRepository crisprAttemptRepository,
        GenotypePrimerRepository genotypePrimerRepository,
        NucleaseRepository nucleaseRepository,
        MutagenesisDonorRepository mutagenesisDonorRepository)
    {
        this.crisprAttemptRepository = crisprAttemptRepository;
        this.genotypePrimerRepository = genotypePrimerRepository;
        this.nucleaseRepository = nucleaseRepository;
        this.mutagenesisDonorRepository = mutagenesisDonorRepository;
    }

    @Override
    public Optional<CrisprAttempt> getCrisprAttemptByPlanId(Long planId)
    {
        return crisprAttemptRepository.findById(planId);
    }

    public List<GenotypePrimer> getGenotypePrimersByCrisprAttempt(CrisprAttempt crisprAttempt)
    {
        return genotypePrimerRepository.findAllByCrisprAttempt(crisprAttempt);
    }

    public List<Nuclease> getNucleasesByCrisprAttempt(CrisprAttempt crisprAttempt)
    {
        return nucleaseRepository.findAllByCrisprAttempt(crisprAttempt);
    }

    public List<MutagenesisDonor> getMutagenesisDonorsByCrisprAttempt(CrisprAttempt crisprAttempt)
    {
        return mutagenesisDonorRepository.findAllByCrisprAttempt(crisprAttempt);
    }
}
