package uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.biology.material_type.MaterialType;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
public class TissueDistributionCentre extends BaseEntity {

    @Id
    @SequenceGenerator(name = "tissueDistributionCentreSeq", sequenceName = "TISSUE_DISTRIBUTION_CENTRE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tissueDistributionCentreSeq")
    private Long id;

    @ManyToOne(targetEntity = PhenotypingProduction.class)
    private PhenotypingProduction phenotypingProduction;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ManyToOne
    private MaterialType materialType;

    @NotNull
    @ManyToOne
    private WorkUnit distributionCentre;

}
