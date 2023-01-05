package org.gentar.biology.plan.attempt.crispr.assay;

import lombok.*;
import org.gentar.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

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
