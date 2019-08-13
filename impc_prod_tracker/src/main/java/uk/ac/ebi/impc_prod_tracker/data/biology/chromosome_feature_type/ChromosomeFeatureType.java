package uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

public class ChromosomeFeatureType {
    @Id
    @SequenceGenerator(name = "alleleTypeSeq", sequenceName = "ALLELE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleTypeSeq")
    private Long id;

    @NotNull
    private String type;
}
