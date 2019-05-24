package uk.ac.ebi.impc_prod_tracker.data.biology.human_gene_disease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanGeneDisease implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = HumanGene.class)
    private HumanGene humanGene;

    @NotNull
    @ManyToOne(targetEntity = HumanDisease.class)
    private HumanDisease humanDisease;

    private boolean humanEvidence;

    private boolean mouseEvidence;

    private String mgi_id;
}
