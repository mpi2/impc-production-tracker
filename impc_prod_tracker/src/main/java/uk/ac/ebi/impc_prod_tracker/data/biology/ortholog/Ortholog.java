package uk.ac.ebi.impc_prod_tracker.data.biology.ortholog;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
@IdClass(Ortholog.class)
public class Ortholog implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private MouseGene mouseGene;

    @Id
    @ManyToOne
    @JoinColumn
    private HumanGene humanGene;

    private String support;

    private Long supportCount;
}
