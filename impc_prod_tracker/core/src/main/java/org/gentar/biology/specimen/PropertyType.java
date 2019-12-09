package org.gentar.biology.specimen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class PropertyType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "propertyTypeSeq", sequenceName = "PROPERTY_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propertyTypeSeq")
    private Long id;

    private String name;

}
