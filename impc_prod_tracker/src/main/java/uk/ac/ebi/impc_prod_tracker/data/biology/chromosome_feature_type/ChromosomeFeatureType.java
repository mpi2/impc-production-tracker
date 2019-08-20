package uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ChromosomeFeatureType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "chromosomeFeatureTypeSeq", sequenceName = "CHROMOSOME_FEATURE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chromosomeFeatureTypeSeq")
    private Long id;

    @NotNull
    private String type;
}
