package org.gentar.biology.plan.attempt.phenotyping;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class PhenotypingAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    private Long imitsPhenotypeAttemptId;

    private Long imitsPhenotypingProductionId;

    @NotNull
    @Column(unique = true)
    private String phenotypingExternalRef;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private Strain strain;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "plan_id")
    private Set<PhenotypingStage> phenotypingStages;
}
