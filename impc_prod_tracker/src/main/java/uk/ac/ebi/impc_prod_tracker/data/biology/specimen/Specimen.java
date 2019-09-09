package uk.ac.ebi.impc_prod_tracker.data.biology.specimen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.outcome.Outcome;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Specimen extends BaseEntity {

    @Id
    @SequenceGenerator(name = "specimenSeq", sequenceName = "SPECIMEN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specimenSeq")
    private Long id;


    @Column(columnDefinition = "TEXT")
    private String specimenExternalRef;

    // TODO revision? - Is this relation correct
    // - will multiple samples be taken from one embryos to characterise different outcomes in the individual embryo?
    @ManyToOne(targetEntity = Outcome.class)
    private Outcome outcome;
}
