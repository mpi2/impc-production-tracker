package org.gentar.biology.mutation;

import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.genbank_file.GenbankFile;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MutationResponseMapperTest
{
    public static final long ID = 1L;
    public static final String MGI_ALLELE_ID = "mgiAlleleId";
    public static final String MGI_ALLELE_SYMBOL = "mgiAlleleSymbol";
    public static final boolean MGI_ALLELE_SYMBOL_REQUIRES_CONSTRUCTION = true;
    public static final boolean MGI_ALLELE_SYMBOL_WITHOUT_IMPC_ABBREVIATION = true;
    public static final boolean ALLELE_CONFIRMED = false;
    public static final String ALLELE_SYMBOL_SUPERSCRIPT_TEMPLATE = "alleleSymbolSuperscriptTemplate";
    public static final String DESCRIPTION = "description";
    public static final String AUTO_DESCRIPTION = "autoDescription";
    public static final long IMITS_ALLELE = 2L;
    public static final String GENETIC_MUTATION_TYPE = "geneticMutationType";
    public static final String MOLECULAR_MUTATION_TYPE = "molecularMutationType";
    public static final String FILE_GB = "fileGb";
    public static final String SYMBOL = "symbol";
    public static final String TPO = "tpo";
    public static final String MUTATION_CATEGORIZATION = "mutationCategorization";

    private MutationResponseMapper testInstance;

    @Mock
    private MutationCommonMapper mutationCommonMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new MutationResponseMapper(mutationCommonMapper);
    }

    @Test
    void toDto()
    {
        Mutation mutation = buildMutation();
        MutationResponseDTO mutationResponseDTO = testInstance.toDto(mutation);

        assertThat(mutationResponseDTO.getId(), is(ID));
        assertThat(mutationResponseDTO.getMgiAlleleId(), is(MGI_ALLELE_ID));
        assertThat(mutationResponseDTO.getMgiAlleleSymbol(), is(MGI_ALLELE_SYMBOL));
        assertThat(
            mutationResponseDTO.getMgiAlleleSymbolWithoutImpcAbbreviation(),
            is(MGI_ALLELE_SYMBOL_WITHOUT_IMPC_ABBREVIATION));
        assertThat(mutationResponseDTO.getDescription(), is(DESCRIPTION));
        assertThat(mutationResponseDTO.getAutoDescription(), is(AUTO_DESCRIPTION));

        verify(mutationCommonMapper, times(1)).toDto(mutation);
    }

    private Mutation buildMutation()
    {
        Mutation mutation = new Mutation();
        mutation.setId(ID);
        mutation.setMgiAlleleId(MGI_ALLELE_ID);
        mutation.setMgiAlleleSymbol(MGI_ALLELE_SYMBOL);
        mutation.setMgiAlleleSymbolRequiresConstruction(MGI_ALLELE_SYMBOL_REQUIRES_CONSTRUCTION);
        mutation.setMgiAlleleSymbolWithoutImpcAbbreviation(MGI_ALLELE_SYMBOL_WITHOUT_IMPC_ABBREVIATION);
        mutation.setAlleleConfirmed(ALLELE_CONFIRMED);
        mutation.setAlleleSymbolSuperscriptTemplate(ALLELE_SYMBOL_SUPERSCRIPT_TEMPLATE);
        mutation.setDescription(DESCRIPTION);
        mutation.setAutoDescription(AUTO_DESCRIPTION);
        mutation.setImitsAllele(IMITS_ALLELE);
        GeneticMutationType geneticMutationType = new GeneticMutationType();
        geneticMutationType.setName(GENETIC_MUTATION_TYPE);
        mutation.setGeneticMutationType(geneticMutationType);
        MolecularMutationType molecularMutationType = new MolecularMutationType();
        molecularMutationType.setName(MOLECULAR_MUTATION_TYPE);
        mutation.setMolecularMutationType(molecularMutationType);
        GenbankFile genbankFile = new GenbankFile();
        genbankFile.setId(1L);
        genbankFile.setName(FILE_GB);
        mutation.setGenbankFile(genbankFile);
        Gene gene = new Gene();
        gene.setSymbol(SYMBOL);
        mutation.setGenes(new HashSet<>(Collections.singletonList(gene)));
        Outcome outcome = new Outcome();
        outcome.setTpo(TPO);
        Plan plan = new Plan();
        plan.setPin("pin1");
        outcome.setPlan(plan);
        mutation.setOutcomes(new HashSet<>(Collections.singletonList(outcome)));
        MutationCategorization mutationCategorization = new MutationCategorization();
        mutationCategorization.setName(MUTATION_CATEGORIZATION);
        mutation.setMutationCategorizations(
            new HashSet<>(Collections.singletonList(mutationCategorization)));
        return mutation;
    }
}
