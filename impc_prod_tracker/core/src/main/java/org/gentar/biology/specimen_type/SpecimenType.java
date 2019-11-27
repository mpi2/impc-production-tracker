package org.gentar.biology.specimen_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class SpecimenType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "specimenTypeSeq", sequenceName = "SPECIMEN_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specimenTypeSeq")
    private Long id;

    private String name;

}
