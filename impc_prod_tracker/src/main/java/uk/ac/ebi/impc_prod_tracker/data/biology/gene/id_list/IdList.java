package uk.ac.ebi.impc_prod_tracker.data.biology.gene.id_list;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class IdList extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "idListSeq", sequenceName = "ID_LIST_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idListSeq")
    private Long id;

    @NotNull
    private String name;
}
