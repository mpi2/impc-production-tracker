package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.assay_type;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class AssayType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "assayTypeSeq", sequenceName = "ASSAY_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assayTypeSeq")
    private Long id;

    private String name;
}
