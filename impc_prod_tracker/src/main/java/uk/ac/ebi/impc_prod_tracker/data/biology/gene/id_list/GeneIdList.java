package uk.ac.ebi.impc_prod_tracker.data.biology.gene.id_list;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GeneIdList extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "genIdListSeq", sequenceName = "GENE_ID_LIST_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genIdListSeq")
    private Long id;

    @NotNull
    @OneToOne(targetEntity = Gene.class)
    private Gene gene;

    @NotNull
    @ManyToOne(targetEntity = IdList.class)
    private IdList idList;

    @NotNull
    private String value;
}
