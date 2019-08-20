package uk.ac.ebi.impc_prod_tracker.data.biology.outcome;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Outcome extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "outcomeSeq", sequenceName = "OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeSeq")
    private Long id;

    @NotNull
    private String tpo;

    @ManyToOne
    private Attempt attempt;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @ManyToMany(mappedBy = "outcomes")
    private Set<Allele> alleles;
}
