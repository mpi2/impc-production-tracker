package org.gentar.biology.colony.distribution.product_type;

import lombok.*;
import org.gentar.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProductType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "productTypeSeq", sequenceName = "PRODUCT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productTypeSeq")
    private Long id;

    private String name;
}
