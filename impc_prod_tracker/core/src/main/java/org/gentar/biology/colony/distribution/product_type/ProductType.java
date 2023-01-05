package org.gentar.biology.colony.distribution.product_type;

import lombok.*;
import org.gentar.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

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
