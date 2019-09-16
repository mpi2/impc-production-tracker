package uk.ac.ebi.impc_prod_tracker.data.biology.sequence.category;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class SequenceCategory  extends BaseEntity {

    @Id
    @SequenceGenerator(name = "sequenceCategorySeq", sequenceName = "SEQUENCE_CATEGORY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceCategorySeq")
    private Long id;

    private String name;
}
