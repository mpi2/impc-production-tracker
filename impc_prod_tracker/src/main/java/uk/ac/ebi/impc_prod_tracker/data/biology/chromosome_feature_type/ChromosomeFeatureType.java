package uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ChromosomeFeatureType {
    @Id
    @SequenceGenerator(name = "alleleTypeSeq", sequenceName = "ALLELE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleTypeSeq")
    private Long id;

    @NotNull
    private String type;
}
