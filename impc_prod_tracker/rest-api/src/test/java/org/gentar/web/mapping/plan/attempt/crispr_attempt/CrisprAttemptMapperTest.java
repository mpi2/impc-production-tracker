package org.gentar.web.mapping.plan.attempt.crispr_attempt;

import org.gentar.biology.plan.attempt.crispr.CrisprAttemptService;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptMapper;
import org.gentar.biology.plan.production.crispr_attempt.AssayDTO;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.biology.plan.production.crispr_attempt.GenotypePrimerDTO;
import org.gentar.biology.plan.production.crispr_attempt.GuideDTO;
import org.gentar.biology.plan.production.crispr_attempt.MutagenesisDonorDTO;
import org.gentar.biology.plan.production.crispr_attempt.NucleaseDTO;
import org.gentar.biology.plan.production.crispr_attempt.ReagentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.assay.AssayType;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationType;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationTypeRepository;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.strain_type.StrainType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CrisprAttemptMapperTest
{
    @Autowired
    private CrisprAttemptMapper crisprAttemptMapper;

    private static final Long IMITS_MI_ATTEMPT_ID = 1L;
    private static final LocalDate MI_DATE = LocalDate.of(1900, 1, 1);
    private static final String MI_EXTERNAL_REF = "externalRef";
    private static final Boolean EXPERIMENTAL_TRUE = true;
    private static final Boolean EXPERIMENTAL_FALSE = false;
    private static final String MUTAGENESIS_EXTERNAL_REF = "mutagenesisExternalRef";
    private static final Integer TOTAL_EMBRYOS_INJECTED = 7;
    private static final Integer TOTAL_EMBRYOS_SURVIVED = 8;
    private static final String EMBRYO_TRANSFER_DAY = "day1";
    private static final String EMBRYO2CELL = "embryoToCell";
    private static final Integer TOTAL_TRANSFERED = 20;
    private static final Integer NUM_FOUNDER_PUPS = 25;
    private static final Integer NUM_FOUNDER_SELECTED_FOR_BREEDING = 26;
    private static final String DELIVERY_METHOD_TYPE_NAME = "deliveryMethodTypeName";
    private static final String COMMENT = "comment";
    private static final Long ASSAY_ID = 1L;
    private static final Long ASSAY_TYPE_ID = 1L;
    private static final String ASSAY_TYPE_NAME = "assayTypeName";
    private static final Integer NUM_FOUNDER_NUM_ASSAYS = 30;
    private static final Integer NUM_HDRG0_MUTANTS = 15;
    private static final Integer NUM_DELETION_G0_MUTANTS = 14;
    private static final Integer NUM_G0_WHERE_MUTATION_DETECTED = 16;
    private static final Integer NUM_HDR_G0_MUTANTS_ALL_DONORS_INSERTED = 17;
    private static final Integer NUM_HDR_G0_MUTANTS_SUBSET_DONORS_INSERTED = 18;
    private static final Integer NUM_NHEJ_G0_MUTANTS = 19;
    private static final Long STRAIN_ID = 1L;
    private static final String MGI_STRAIN_ACC_ID = "MgiStrainAccId";
    private static final String STRAIN_NAME = "Name";
    private static final String STRAIN_TYPE_NAME= "Type Name";
    private static final Long GUIDE_ID_1 = 1L;
    private static final Long GUIDE_ID_2 = 2L;
    private static final String GUIDE_SEQ = "seq";
    private static final String GUIDE_CHR = "chr";
    private static final Integer GUIDE_START_1 = 1;
    private static final Integer GUIDE_STOP_1 = 2;
    private static final Integer GUIDE_START_2 = 3;
    private static final Integer GUIDE_STOP_2 = 4;
    private static final Boolean GUIDE_TRUNCATED_TRUE = true;
    private static final Boolean GUIDE_TRUNCATED_FALSE = false;
    private static final String GUIDE_STRAND = "strand";
    private static final String GUIDE_GENOME_BUILD = "genomeBuild";
    private static final String GUIDE_PAM3 = "pam3_";
    private static final String GUIDE_PAM5 = "pam5_";
    private static final String GUIDE_PROTOSPACER_SEQUENCE = "protospacerSequence";
    private static final String NUCLEASE_TYPE_NAME = "NucleaseTypeName1";
    private static final Long NUCLEASE_ID1 = 1L;
    private static final Long NUCLEASE_ID2 = 2L;
    private static final Double NUCLEASE_CONCENTRATION_1 = 30.0;
    private static final Double NUCLEASE_CONCENTRATION_2 = 50.0;
    private static final Long MUTAGENESIS_DONOR_ID1 = 1L;
    private static final String MUTAGENESIS_DONOR_VECTOR_NAME = "Vector Name";
    private static final Double MUTAGENESIS_DONOR_CONCENTRATION_1 = 30.0;
    private static final Double MUTAGENESIS_DONOR_CONCENTRATION_2 = 40.0;
    private static final String MUTAGENESIS_DONOR_PREPARATION = "Preparation";
    private static final String MUTAGENESIS_DONOR_OLIGO_SEQUENCE_FA = "Oligosequence FA";
    private static final Long MUTAGENESIS_DONOR_ID2 = 2L;
    private static final Long REAGENT_ID_1 = 1L;
    private static final Long REAGENT_ID_2 = 2L;
    private static final String REAGENT_NAME = "Reagent Name";
    private static final String REAGENT_DESCRIPTION = "Reagent description";
    private static final double REAGENT_CONCENTRATION_1 = 20.0;
    private static final double REAGENT_CONCENTRATION_2 = 40.0;
    private static final String GENOTYPE_PRIMER_NAME = "GenotypeName";
    private static final String GENOTYPE_PRIMER_SEQUENCE = "GenotypeSequence";
    private static final Long GENOTYPE_PRIMER_ID_1 = 1L;
    private static final Long GENOTYPE_PRIMER_ID_2 = 2L;
    private static final Integer GENOTYPE_PRIMER_START_1 = 20;
    private static final Integer GENOTYPE_PRIMER_STOP_1 = 25;
    private static final Integer GENOTYPE_PRIMER_START_2 = 30;
    private static final Integer GENOTYPE_PRIMER_STOP_2 = 35;

    @MockBean @Autowired
    CrisprAttemptService crisprAttemptService;

    @MockBean @Autowired
    PreparationTypeRepository preparationTypeRepository;

    @Test
    public void testToDto()
    {
        CrisprAttempt crisprAttempt = buildCrisprAttempt();
        addGuides(crisprAttempt);
        addGenotypePrimers(crisprAttempt);

        CrisprAttemptDTO crisprAttemptDTO = crisprAttemptMapper.toDto(crisprAttempt);

        validateDto(crisprAttemptDTO);
    }

    private void validateDto(CrisprAttemptDTO crisprAttemptDTO)
    {
        assertThat("ImitsMiAttemptId", crisprAttemptDTO.getImitsMiAttemptId(), is(IMITS_MI_ATTEMPT_ID));
        assertThat(
            "MiDate", crisprAttemptDTO.getMiDate(), is(MI_DATE));
        assertThat("MiExternalRef", crisprAttemptDTO.getMiExternalRef(), is(MI_EXTERNAL_REF));
        assertThat("Experimental", crisprAttemptDTO.getExperimental(), is(true));
        assertThat(
            "MutagenesisExternalRef",
            crisprAttemptDTO.getMutagenesisExternalRef(),
            is(MUTAGENESIS_EXTERNAL_REF));
        assertThat(
            "TotalEmbryosInjected",
            crisprAttemptDTO.getTotalEmbryosInjected(),
            is(TOTAL_EMBRYOS_INJECTED));
        assertThat(
            "TotalEmbryosSurvived",
            crisprAttemptDTO.getTotalEmbryosSurvived(),
            is(TOTAL_EMBRYOS_SURVIVED));
        assertThat("Embryo2Cell", crisprAttemptDTO.getEmbryo2Cell(), is(EMBRYO2CELL));
        assertThat("Comment", crisprAttemptDTO.getComment(), is(COMMENT));
        assertThat("Assay", crisprAttemptDTO.getAssay(), notNullValue());
        assertThat(
            "Assay TypeName", crisprAttemptDTO.getAssay().getTypeName(), is(ASSAY_TYPE_NAME));
        assertThat(
                "Assay EmbryoTransferDay", crisprAttemptDTO.getAssay().getEmbryoTransferDay(), is(EMBRYO_TRANSFER_DAY));
        assertThat(
                "Assay NumFoundersSelectedForBreeding",
                crisprAttemptDTO.getAssay().getNumFounderSelectedForBreeding(),
                is(NUM_FOUNDER_SELECTED_FOR_BREEDING));
        assertThat("Assay TotalTransferred", crisprAttemptDTO.getAssay().getTotalTransferred(), is(TOTAL_TRANSFERED));
        assertThat("Assay NumFounderPups", crisprAttemptDTO.getAssay().getNumFounderPups(), is(NUM_FOUNDER_PUPS));
        assertThat(
            "Assay FounderNumAssays",
            crisprAttemptDTO.getAssay().getFounderNumAssays(),
            is(NUM_FOUNDER_NUM_ASSAYS));
        assertThat(
            "Assay NumHdrG0Mutants",
            crisprAttemptDTO.getAssay().getNumHdrG0Mutants(),
            is(NUM_HDRG0_MUTANTS));
        assertThat(
            "Assay NumDeletionG0Mutants",
            crisprAttemptDTO.getAssay().getNumDeletionG0Mutants(),
            is(NUM_DELETION_G0_MUTANTS));
        assertThat(
            "Assay NumG0WhereMutationDetected",
            crisprAttemptDTO.getAssay().getNumG0WhereMutationDetected(),
            is(NUM_G0_WHERE_MUTATION_DETECTED));
        assertThat(
            "Assay NumHdrG0MutantsAllDonorsInserted",
            crisprAttemptDTO.getAssay().getNumHdrG0MutantsAllDonorsInserted(),
            is(NUM_HDR_G0_MUTANTS_ALL_DONORS_INSERTED));
        assertThat(
            "Assay NumHdrG0MutantsSubsetDonorsInserted",
            crisprAttemptDTO.getAssay().getNumHdrG0MutantsSubsetDonorsInserted(),
            is(NUM_HDR_G0_MUTANTS_SUBSET_DONORS_INSERTED));
        assertThat(
            "Assay NumNhejG0Mutants",
            crisprAttemptDTO.getAssay().getNumNhejG0Mutants(),
            is(NUM_NHEJ_G0_MUTANTS));
        assertThat("Strain", crisprAttemptDTO.getStrainName(), notNullValue());
        assertThat("Strain Name", crisprAttemptDTO.getStrainName(), is(STRAIN_NAME));
        assertThat("Guides", crisprAttemptDTO.getGuideDTOS(), notNullValue());

        GuideDTO guideDTO1 = findGuideDtoById(crisprAttemptDTO.getGuideDTOS(), GUIDE_ID_1);
        assertThat("Guides 1 null", guideDTO1, notNullValue());
        assertThat("Guides 1 sequence", guideDTO1.getSequence(), is (GUIDE_SEQ + GUIDE_ID_1));
        assertThat("Guides 1 start ", guideDTO1.getStart(), is (GUIDE_START_1));
        assertThat("Guides 1 stop ", guideDTO1.getStop(), is (GUIDE_STOP_1));
        assertThat("Guides 1 chr ", guideDTO1.getChr(), is (GUIDE_CHR + GUIDE_ID_1));
        assertThat("Guides 1 truncated ", guideDTO1.getTruncatedGuide(), is(GUIDE_TRUNCATED_TRUE));
        assertThat("Guides 1 strand ", guideDTO1.getStrand(), is(GUIDE_STRAND + GUIDE_ID_1));
        assertThat(
            "Guides 1 Genome Build ", guideDTO1.getGenomeBuild(), is(GUIDE_GENOME_BUILD + GUIDE_ID_1));
        assertThat("Guides 1 Pam3 ", guideDTO1.getPam3(), is(GUIDE_PAM3 + GUIDE_ID_1));
        assertThat("Guides 1 Pam5 ", guideDTO1.getPam5(), is(GUIDE_PAM5 + GUIDE_ID_1));
        assertThat(
            "Guides 1 ProtospacerSequence ",
            guideDTO1.getProtospacerSequence(),
            is(GUIDE_PROTOSPACER_SEQUENCE + GUIDE_ID_1));

        GuideDTO guideDTO2 = findGuideDtoById(crisprAttemptDTO.getGuideDTOS(), GUIDE_ID_2);
        assertThat("Guides 2 null", guideDTO2, notNullValue());
        assertThat("Guides 2 sequence", guideDTO2.getSequence(), is (GUIDE_SEQ + GUIDE_ID_2));
        assertThat("Guides 2 start ", guideDTO2.getStart(), is (GUIDE_START_2));
        assertThat("Guides 2 stop ", guideDTO2.getStop(), is (GUIDE_STOP_2));
        assertThat("Guides 2 chr ", guideDTO2.getChr(), is (GUIDE_CHR + GUIDE_ID_2));
        assertThat("Guides 2 truncated ", guideDTO2.getTruncatedGuide(), is(GUIDE_TRUNCATED_FALSE));
        assertThat("Guides 2 strand ", guideDTO2.getStrand(), is(GUIDE_STRAND + GUIDE_ID_2));
        assertThat(
            "Guides 2 Genome Build ", guideDTO2.getGenomeBuild(), is(GUIDE_GENOME_BUILD + GUIDE_ID_2));
        assertThat("Guides 2 Pam3 ", guideDTO2.getPam3(), is(GUIDE_PAM3 + GUIDE_ID_2));
        assertThat("Guides 2 Pam5 ", guideDTO2.getPam5(), is(GUIDE_PAM5 + GUIDE_ID_2));
        assertThat(
            "Guides 2 ProtospacerSequence ",
            guideDTO2.getProtospacerSequence(),
            is(GUIDE_PROTOSPACER_SEQUENCE + GUIDE_ID_2));

        System.out.println("----"+crisprAttemptDTO.getGenotypePrimerDTOS());
        GenotypePrimerDTO genotypePrimerDTO1 =
            findGenotypePrimerDTOById(crisprAttemptDTO.getGenotypePrimerDTOS(), GENOTYPE_PRIMER_ID_1);
        assertThat("Genotype Primer 1", genotypePrimerDTO1, notNullValue());
        assertThat(
            "genotypePrimerDTO1 sequence",
            genotypePrimerDTO1.getSequence(),
            is (GENOTYPE_PRIMER_SEQUENCE + GENOTYPE_PRIMER_ID_1));
        assertThat(
            "genotypePrimerDTO1 1 start ",
            genotypePrimerDTO1.getGenomicStartCoordinate(),
            is (GENOTYPE_PRIMER_START_1));
        assertThat(
            "genotypePrimerDTO1 1 stop ",
            genotypePrimerDTO1.getGenomicEndCoordinate(),
            is (GENOTYPE_PRIMER_STOP_1));

        GenotypePrimerDTO genotypePrimerDTO2 =
            findGenotypePrimerDTOById(crisprAttemptDTO.getGenotypePrimerDTOS(), GENOTYPE_PRIMER_ID_2);
        assertThat("Genotype Primer 2", genotypePrimerDTO2, notNullValue());
        assertThat(
            "genotypePrimerDTO2 sequence",
            genotypePrimerDTO2.getSequence(),
            is (GENOTYPE_PRIMER_SEQUENCE + GENOTYPE_PRIMER_ID_2));
        assertThat(
            "genotypePrimerDTO2 start ",
            genotypePrimerDTO2.getGenomicStartCoordinate(),
            is (GENOTYPE_PRIMER_START_2));
        assertThat(
            "genotypePrimerDTO2 stop ",
            genotypePrimerDTO2.getGenomicEndCoordinate(),
            is (GENOTYPE_PRIMER_STOP_2));

    }

    private GuideDTO findGuideDtoById(Collection<GuideDTO> guideDTOS, Long id)
    {
         return guideDTOS.stream().filter(t -> id.equals(t.getId())).findFirst().orElse(null);
    }

    private Guide findGuideById(Collection<Guide> guides, Long id)
    {
        return guides.stream().filter(t -> id.equals(t.getId())).findFirst().orElse(null);
    }

    private GenotypePrimerDTO findGenotypePrimerDTOById(
        Collection<GenotypePrimerDTO> genotypePrimerDTOS, Long id)
    {
        return genotypePrimerDTOS.stream().filter(t -> id.equals(t.getId())).findFirst().orElse(null);
    }

    private void addGuides(CrisprAttempt crisprAttempt)
    {
        Guide guide1 = buildGuide(GUIDE_ID_1, GUIDE_START_1, GUIDE_STOP_1, GUIDE_TRUNCATED_TRUE);
        Guide guide2 = buildGuide(GUIDE_ID_2, GUIDE_START_2, GUIDE_STOP_2, GUIDE_TRUNCATED_FALSE);

        Set<Guide> guides = new HashSet<>();
        guides.add(guide1);
        guides.add(guide2);

        crisprAttempt.setGuides(guides);
    }

    private Guide buildGuide(Long id, int start, int stop, boolean truncated)
    {
        Guide guide = new Guide();
        guide.setId(id);
        guide.setSequence(GUIDE_SEQ + id);
        guide.setStart(start);
        guide.setStop(stop);
        guide.setChr(GUIDE_CHR + id);
        guide.setTruncatedGuide(truncated);
        guide.setStrand(GUIDE_STRAND + id);
        guide.setGenomeBuild(GUIDE_GENOME_BUILD + id);
        guide.setPam3(GUIDE_PAM3 + id);
        guide.setPam5(GUIDE_PAM5 + id);
        guide.setProtospacerSequence(GUIDE_PROTOSPACER_SEQUENCE + id);

        return guide;
    }

    private void addGenotypePrimers(CrisprAttempt crisprAttempt)
    {
        Set<GenotypePrimer> genotypePrimers = new HashSet<>();
        GenotypePrimer genotypePrimer1 =
            buildGenotypePrimer(GENOTYPE_PRIMER_ID_1, GENOTYPE_PRIMER_START_1, GENOTYPE_PRIMER_STOP_1);
        GenotypePrimer genotypePrimer2 =
            buildGenotypePrimer(GENOTYPE_PRIMER_ID_2, GENOTYPE_PRIMER_START_2, GENOTYPE_PRIMER_STOP_2);
        genotypePrimers.add(genotypePrimer1);
        genotypePrimers.add(genotypePrimer2);
        crisprAttempt.setPrimers(genotypePrimers);
    }

    private GenotypePrimer buildGenotypePrimer(Long id, Integer start, Integer end)
    {
        GenotypePrimer genotypePrimer = new GenotypePrimer();
        genotypePrimer.setId(id);
        genotypePrimer.setGenomicStartCoordinate(start);
        genotypePrimer.setGenomicEndCoordinate(end);
        genotypePrimer.setSequence(GENOTYPE_PRIMER_SEQUENCE + id);

        return genotypePrimer;
    }

    private CrisprAttempt buildCrisprAttempt()
    {
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setImitsMiAttemptId(IMITS_MI_ATTEMPT_ID);
        crisprAttempt.setMiDate(MI_DATE);
        crisprAttempt.setMiExternalRef(MI_EXTERNAL_REF);
        crisprAttempt.setExperimental(EXPERIMENTAL_TRUE);
        crisprAttempt.setMutagenesisExternalRef(MUTAGENESIS_EXTERNAL_REF);
        crisprAttempt.setTotalEmbryosInjected(TOTAL_EMBRYOS_INJECTED);
        crisprAttempt.setTotalEmbryosSurvived(TOTAL_EMBRYOS_SURVIVED);
        crisprAttempt.setEmbryo2Cell(EMBRYO2CELL);
        crisprAttempt.setComment(COMMENT);
        Assay assay = new Assay();
        assay.setId(ASSAY_ID);
        AssayType assayType = new AssayType();
        assayType.setId(ASSAY_TYPE_ID);
        assayType.setName(ASSAY_TYPE_NAME);
        assay.setAssayType(assayType);
        assay.setEmbryoTransferDay(EMBRYO_TRANSFER_DAY);
        assay.setTotalTransferred(TOTAL_TRANSFERED);
        assay.setNumFounderPups(NUM_FOUNDER_PUPS);
        assay.setNumFounderSelectedForBreeding(NUM_FOUNDER_SELECTED_FOR_BREEDING);
        assay.setFounderNumAssays(NUM_FOUNDER_NUM_ASSAYS);
        assay.setNumHdrG0Mutants(NUM_HDRG0_MUTANTS);
        assay.setNumDeletionG0Mutants(NUM_DELETION_G0_MUTANTS);
        assay.setNumG0WhereMutationDetected(NUM_G0_WHERE_MUTATION_DETECTED);
        assay.setNumHdrG0MutantsAllDonorsInserted(NUM_HDR_G0_MUTANTS_ALL_DONORS_INSERTED);
        assay.setNumHdrG0MutantsSubsetDonorsInserted(NUM_HDR_G0_MUTANTS_SUBSET_DONORS_INSERTED);
        assay.setNumNhejG0Mutants(NUM_NHEJ_G0_MUTANTS);
        crisprAttempt.setAssay(assay);
        Strain strain = new Strain();
        strain.setId(STRAIN_ID);
        strain.setMgiStrainAccId(MGI_STRAIN_ACC_ID);
        strain.setName(STRAIN_NAME);
        StrainType strainType = new StrainType();
        strainType.setName(STRAIN_TYPE_NAME);
        Set<StrainType> strainTypeSet = new HashSet<>();
        strainTypeSet.add(strainType);
        strain.setStrainTypes(strainTypeSet);
        crisprAttempt.setStrain(strain);

        return crisprAttempt;
    }

    @Test
    public void testToEntity()
    {
        PreparationType preparationType1 = new PreparationType();
        preparationType1.setName(MUTAGENESIS_DONOR_PREPARATION + MUTAGENESIS_DONOR_ID1);
        PreparationType preparationType2 = new PreparationType();
        preparationType1.setName(MUTAGENESIS_DONOR_PREPARATION + MUTAGENESIS_DONOR_ID2);
        AssayType assayType = new AssayType();
        assayType.setName(ASSAY_TYPE_NAME);
        when(preparationTypeRepository.findFirstByName(
            MUTAGENESIS_DONOR_PREPARATION + MUTAGENESIS_DONOR_ID1))
            .thenReturn(preparationType1);
        when(preparationTypeRepository.findFirstByName(
            MUTAGENESIS_DONOR_PREPARATION + MUTAGENESIS_DONOR_ID2))
            .thenReturn(preparationType2);
        when(crisprAttemptService.getAssayTypeByName(ASSAY_TYPE_NAME)).thenReturn(assayType);

        CrisprAttemptDTO crisprAttemptDTO = buildCrisprAttemptDto();
        addNucleaseDTOs(crisprAttemptDTO);
        addGuideDTOs(crisprAttemptDTO);
        addMutagenesisDonorDTOs(crisprAttemptDTO);
        addReagentDTOs(crisprAttemptDTO);
        addGenotypePrimerDTOs(crisprAttemptDTO);

        CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(crisprAttemptDTO);

        System.out.println(crisprAttempt);

        validateEntity(crisprAttempt);
    }

    private void validateEntity(CrisprAttempt crisprAttempt)
    {
        assertThat("crisprAttempt", crisprAttempt, notNullValue());
        assertThat("ImitsMiAttemptId", crisprAttempt.getImitsMiAttemptId(), is(IMITS_MI_ATTEMPT_ID));
        assertThat(
            "MiDate", crisprAttempt.getMiDate(), is(MI_DATE));
        assertThat("MiExternalRef", crisprAttempt.getMiExternalRef(), is(MI_EXTERNAL_REF));
        assertThat("Experimental", crisprAttempt.getExperimental(), is(true));
        assertThat(
            "MutagenesisExternalRef",
            crisprAttempt.getMutagenesisExternalRef(),
            is(MUTAGENESIS_EXTERNAL_REF));
        assertThat(
            "TotalEmbryosInjected",
            crisprAttempt.getTotalEmbryosInjected(),
            is(TOTAL_EMBRYOS_INJECTED));
        assertThat(
            "TotalEmbryosSurvived",
            crisprAttempt.getTotalEmbryosSurvived(),
            is(TOTAL_EMBRYOS_SURVIVED));
        assertThat("Embryo2Cell", crisprAttempt.getEmbryo2Cell(), is(EMBRYO2CELL));
        assertThat("Comment", crisprAttempt.getComment(), is(COMMENT));
        assertThat("Assay", crisprAttempt.getAssay(), notNullValue());
        assertThat(
            "Assay TypeName", crisprAttempt.getAssay().getAssayType().getName(), is(ASSAY_TYPE_NAME));
        assertThat(
                "EmbryoTransferDay", crisprAttempt.getAssay().getEmbryoTransferDay(), is(EMBRYO_TRANSFER_DAY));
        assertThat("TotalTransferred", crisprAttempt.getAssay().getTotalTransferred(), is(TOTAL_TRANSFERED));
        assertThat("NumFounderPups", crisprAttempt.getAssay().getNumFounderPups(), is(NUM_FOUNDER_PUPS));
        assertThat(
                "NumFoundersSelectedForBreeding",
                crisprAttempt.getAssay().getNumFounderSelectedForBreeding(),
                is(NUM_FOUNDER_SELECTED_FOR_BREEDING));
        assertThat(
            "Assay FounderNumAssays",
            crisprAttempt.getAssay().getFounderNumAssays(),
            is(NUM_FOUNDER_NUM_ASSAYS));
        assertThat(
            "Assay NumHdrG0Mutants",
            crisprAttempt.getAssay().getNumHdrG0Mutants(),
            is(NUM_HDRG0_MUTANTS));
        assertThat(
            "Assay NumDeletionG0Mutants",
            crisprAttempt.getAssay().getNumDeletionG0Mutants(),
            is(NUM_DELETION_G0_MUTANTS));
        assertThat(
            "Assay NumG0WhereMutationDetected",
            crisprAttempt.getAssay().getNumG0WhereMutationDetected(),
            is(NUM_G0_WHERE_MUTATION_DETECTED));
        assertThat(
            "Assay NumHdrG0MutantsAllDonorsInserted",
            crisprAttempt.getAssay().getNumHdrG0MutantsAllDonorsInserted(),
            is(NUM_HDR_G0_MUTANTS_ALL_DONORS_INSERTED));
        assertThat(
            "Assay NumHdrG0MutantsSubsetDonorsInserted",
            crisprAttempt.getAssay().getNumHdrG0MutantsSubsetDonorsInserted(),
            is(NUM_HDR_G0_MUTANTS_SUBSET_DONORS_INSERTED));
        assertThat(
            "Assay NumNhejG0Mutants",
            crisprAttempt.getAssay().getNumNhejG0Mutants(),
            is(NUM_NHEJ_G0_MUTANTS));
        assertThat("Strain", crisprAttempt.getStrain(), notNullValue());
        assertThat("Strain MgiId", crisprAttempt.getStrain().getMgiStrainAccId(), is(MGI_STRAIN_ACC_ID));
        assertThat("Strain Name", crisprAttempt.getStrain().getName(), is(STRAIN_NAME));
        assertThat("Guides", crisprAttempt.getGuides(), notNullValue());

        Guide guide1 = findGuideById(crisprAttempt.getGuides(), GUIDE_ID_1);
        assertThat("Guides 1 null", guide1, notNullValue());
        assertThat("Guides 1 sequence", guide1.getSequence(), is (GUIDE_SEQ + GUIDE_ID_1));
        assertThat("Guides 1 start ", guide1.getStart(), is (GUIDE_START_1));
        assertThat("Guides 1 stop ", guide1.getStop(), is (GUIDE_STOP_1));
        assertThat("Guides 1 chr ", guide1.getChr(), is (GUIDE_CHR + GUIDE_ID_1));
        assertThat("Guides 1 truncated ", guide1.getTruncatedGuide(), is(GUIDE_TRUNCATED_TRUE));
        assertThat("Guides 1 strand ", guide1.getStrand(), is(GUIDE_STRAND + GUIDE_ID_1));
        assertThat(
            "Guides 1 Genome Build ", guide1.getGenomeBuild(), is(GUIDE_GENOME_BUILD + GUIDE_ID_1));
        assertThat("Guides 1 Pam3 ", guide1.getPam3(), is(GUIDE_PAM3 + GUIDE_ID_1));
        assertThat("Guides 1 Pam5 ", guide1.getPam5(), is(GUIDE_PAM5 + GUIDE_ID_1));
        assertThat(
            "Guides 1 ProtospacerSequence ",
            guide1.getProtospacerSequence(),
            is(GUIDE_PROTOSPACER_SEQUENCE + GUIDE_ID_1));

        Guide guide2 = findGuideById(crisprAttempt.getGuides(), GUIDE_ID_2);
        assertThat("Guides 2 null", guide2, notNullValue());
        assertThat("Guides 2 sequence", guide2.getSequence(), is (GUIDE_SEQ + GUIDE_ID_2));
        assertThat("Guides 2 start ", guide2.getStart(), is (GUIDE_START_2));
        assertThat("Guides 2 stop ", guide2.getStop(), is (GUIDE_STOP_2));
        assertThat("Guides 2 chr ", guide2.getChr(), is (GUIDE_CHR + GUIDE_ID_2));
        assertThat("Guides 2 truncated ", guide2.getTruncatedGuide(), is(GUIDE_TRUNCATED_FALSE));
        assertThat("Guides 2 strand ", guide2.getStrand(), is(GUIDE_STRAND + GUIDE_ID_2));
        assertThat(
            "Guides 2 Genome Build ", guide2.getGenomeBuild(), is(GUIDE_GENOME_BUILD + GUIDE_ID_2));
        assertThat("Guides 2 Pam3 ", guide2.getPam3(), is(GUIDE_PAM3 + GUIDE_ID_2));
        assertThat("Guides 2 Pam5 ", guide2.getPam5(), is(GUIDE_PAM5 + GUIDE_ID_2));
        assertThat(
            "Guides 2 ProtospacerSequence ",
            guide2.getProtospacerSequence(),
            is(GUIDE_PROTOSPACER_SEQUENCE + GUIDE_ID_2));
    }

    private void addNucleaseDTOs(CrisprAttemptDTO crisprAttemptDTO)
    {
        List<NucleaseDTO> nucleaseDTOS = new ArrayList<>();
        nucleaseDTOS.add(buildNucleaseDTO(NUCLEASE_ID1, NUCLEASE_CONCENTRATION_1));
        nucleaseDTOS.add(buildNucleaseDTO(NUCLEASE_ID2, NUCLEASE_CONCENTRATION_2));
        crisprAttemptDTO.setNucleaseDTOS(nucleaseDTOS);
    }

    private NucleaseDTO buildNucleaseDTO(Long id, Double concentration)
    {
        NucleaseDTO nucleaseDTO = new NucleaseDTO();
        nucleaseDTO.setId(id);
        nucleaseDTO.setConcentration(concentration);
        nucleaseDTO.setTypeName(NUCLEASE_TYPE_NAME + id);

        return nucleaseDTO;
    }

    private void addGuideDTOs(CrisprAttemptDTO crisprAttemptDTO)
    {
        List<GuideDTO> guideDTOS = new ArrayList<>();
        guideDTOS.add(buildGuideDTO(GUIDE_ID_1, GUIDE_START_1, GUIDE_STOP_1, GUIDE_TRUNCATED_TRUE));
        guideDTOS.add(buildGuideDTO(GUIDE_ID_2, GUIDE_START_2, GUIDE_STOP_2, GUIDE_TRUNCATED_FALSE));
        crisprAttemptDTO.setGuideDTOS(guideDTOS);
    }

    private GuideDTO buildGuideDTO(Long id, int start, int stop, boolean truncated)
    {
        GuideDTO guideDTO = new GuideDTO();
        guideDTO.setId(id);
        guideDTO.setSequence(GUIDE_SEQ + id);
        guideDTO.setStart(start);
        guideDTO.setStop(stop);
        guideDTO.setChr(GUIDE_CHR + id);
        guideDTO.setTruncatedGuide(truncated);
        guideDTO.setStrand(GUIDE_STRAND + id);
        guideDTO.setGenomeBuild(GUIDE_GENOME_BUILD + id);
        guideDTO.setPam3(GUIDE_PAM3 + id);
        guideDTO.setPam5(GUIDE_PAM5 + id);
        guideDTO.setProtospacerSequence(GUIDE_PROTOSPACER_SEQUENCE + id);

        return guideDTO;
    }

    private void addMutagenesisDonorDTOs(CrisprAttemptDTO crisprAttemptDTO)
    {
        List<MutagenesisDonorDTO> mutagenesisDonorDTOS = new ArrayList<>();
        mutagenesisDonorDTOS.add(
                buildMutagenesisDonorDTO(MUTAGENESIS_DONOR_ID1, MUTAGENESIS_DONOR_CONCENTRATION_1));
        mutagenesisDonorDTOS.add(
                buildMutagenesisDonorDTO(MUTAGENESIS_DONOR_ID2, MUTAGENESIS_DONOR_CONCENTRATION_2));
        crisprAttemptDTO.setMutagenesisDonorDTOS(mutagenesisDonorDTOS);
    }

    private MutagenesisDonorDTO buildMutagenesisDonorDTO(Long id, Double concentration)
    {
        MutagenesisDonorDTO mutagenesisDonorDTO = new MutagenesisDonorDTO();
        mutagenesisDonorDTO.setId(id);
        mutagenesisDonorDTO.setConcentration(concentration);
        mutagenesisDonorDTO.setPreparationTypeName(MUTAGENESIS_DONOR_PREPARATION + id);
        mutagenesisDonorDTO.setVectorName(MUTAGENESIS_DONOR_VECTOR_NAME  + id);
        mutagenesisDonorDTO.setOligoSequenceFasta(MUTAGENESIS_DONOR_OLIGO_SEQUENCE_FA  + id);
        return mutagenesisDonorDTO;
    }

    private void addReagentDTOs(CrisprAttemptDTO crisprAttemptDTO)
    {
        List<ReagentDTO> reagentDTOS = new ArrayList<>();
        reagentDTOS.add(buildReagentDTO(REAGENT_ID_1, REAGENT_CONCENTRATION_1));
        reagentDTOS.add(buildReagentDTO(REAGENT_ID_2, REAGENT_CONCENTRATION_2));
        crisprAttemptDTO.setReagentDTOS(reagentDTOS);
    }

    private ReagentDTO buildReagentDTO(Long id, Double concentration)
    {
        ReagentDTO reagentDTO = new ReagentDTO();
        reagentDTO.setId(id);
        reagentDTO.setName(REAGENT_NAME + id);
        reagentDTO.setDescription(REAGENT_DESCRIPTION + id);
        reagentDTO.setConcentration(concentration);
        return reagentDTO;
    }

    private void addGenotypePrimerDTOs(CrisprAttemptDTO crisprAttemptDTO)
    {
        List<GenotypePrimerDTO> genotypePrimerDTOS = new ArrayList<>();
        genotypePrimerDTOS.add(
            buildGenotypePrimerDTO(
                GENOTYPE_PRIMER_ID_1, GENOTYPE_PRIMER_START_1, GENOTYPE_PRIMER_STOP_1));
        genotypePrimerDTOS.add(
            buildGenotypePrimerDTO(
                GENOTYPE_PRIMER_ID_2, GENOTYPE_PRIMER_START_2, GENOTYPE_PRIMER_STOP_2));
        crisprAttemptDTO.setGenotypePrimerDTOS(genotypePrimerDTOS);
    }

    private GenotypePrimerDTO buildGenotypePrimerDTO(Long id, Integer start, Integer stop)
    {
        GenotypePrimerDTO genotypePrimerDTO = new GenotypePrimerDTO();
        genotypePrimerDTO.setId(id);
        genotypePrimerDTO.setName(GENOTYPE_PRIMER_NAME + id);
        genotypePrimerDTO.setGenomicStartCoordinate(start);
        genotypePrimerDTO.setGenomicEndCoordinate(stop);
        genotypePrimerDTO.setSequence(GENOTYPE_PRIMER_SEQUENCE + id);
        return genotypePrimerDTO;

    }

    private CrisprAttemptDTO buildCrisprAttemptDto()
    {
        CrisprAttemptDTO crisprAttemptDTO = new CrisprAttemptDTO();
        crisprAttemptDTO.setImitsMiAttemptId(IMITS_MI_ATTEMPT_ID);
        crisprAttemptDTO.setMiDate(MI_DATE);
        crisprAttemptDTO.setMiExternalRef(MI_EXTERNAL_REF);
        crisprAttemptDTO.setExperimental(EXPERIMENTAL_TRUE);
        crisprAttemptDTO.setMutagenesisExternalRef(MUTAGENESIS_EXTERNAL_REF);
        crisprAttemptDTO.setTotalEmbryosInjected(TOTAL_EMBRYOS_INJECTED);
        crisprAttemptDTO.setTotalEmbryosSurvived(TOTAL_EMBRYOS_SURVIVED);
        crisprAttemptDTO.setEmbryo2Cell(EMBRYO2CELL);
        crisprAttemptDTO.setComment(COMMENT);
        AssayDTO assayDTO = new AssayDTO();
        assayDTO.setId(ASSAY_ID);
        assayDTO.setTypeName(ASSAY_TYPE_NAME);
        assayDTO.setEmbryoTransferDay(EMBRYO_TRANSFER_DAY);
        assayDTO.setTotalTransferred(TOTAL_TRANSFERED);
        assayDTO.setNumFounderPups(NUM_FOUNDER_PUPS);
        assayDTO.setNumFounderSelectedForBreeding(NUM_FOUNDER_SELECTED_FOR_BREEDING);
        assayDTO.setFounderNumAssays(NUM_FOUNDER_NUM_ASSAYS);
        assayDTO.setNumHdrG0Mutants(NUM_HDRG0_MUTANTS);
        assayDTO.setNumDeletionG0Mutants(NUM_DELETION_G0_MUTANTS);
        assayDTO.setNumG0WhereMutationDetected(NUM_G0_WHERE_MUTATION_DETECTED);
        assayDTO.setNumHdrG0MutantsAllDonorsInserted(NUM_HDR_G0_MUTANTS_ALL_DONORS_INSERTED);
        assayDTO.setNumHdrG0MutantsSubsetDonorsInserted(NUM_HDR_G0_MUTANTS_SUBSET_DONORS_INSERTED);
        assayDTO.setNumNhejG0Mutants(NUM_NHEJ_G0_MUTANTS);
        crisprAttemptDTO.setAssay(assayDTO);
        crisprAttemptDTO.setStrainName(STRAIN_NAME);

        return crisprAttemptDTO;
    }
}