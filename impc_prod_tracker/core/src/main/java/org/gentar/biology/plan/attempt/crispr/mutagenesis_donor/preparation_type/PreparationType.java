package org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type;

import lombok.Data;
import org.gentar.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class PreparationType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "preparationTypeSeq", sequenceName = "PREPARATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preparationTypeSeq")
    private Long id;

    private String name;
}
