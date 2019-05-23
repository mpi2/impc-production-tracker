package uk.ac.ebi.impc_prod_tracker.data.biology.omim_table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;

import javax.persistence.*;
import java.util.Set;


@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class OmimTable extends BaseEntity {

    @Id
    @SequenceGenerator(name = "OmimTableSeq", sequenceName = "OMIM_TABLE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OmimTableSeq")
    private Long id;

    private String omimId;

    @ManyToMany(mappedBy = "omimTable")
    private Set<HumanDisease> humanDiseases;
}
