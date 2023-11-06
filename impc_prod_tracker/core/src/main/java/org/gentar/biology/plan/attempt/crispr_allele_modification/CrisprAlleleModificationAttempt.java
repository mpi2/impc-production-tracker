package org.gentar.biology.plan.attempt.crispr_allele_modification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class CrisprAlleleModificationAttempt extends BaseEntity
{

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    @Column(name = "number_of_cre_matings_successful")
    private Integer numberOfCreMatingsSuccessful;

    private String modificationExternalRef;

    @Column(name = "tat_cre", columnDefinition = "boolean default false")
    private Boolean tatCre;

    @ManyToOne
    private Strain deleterStrain;


}
