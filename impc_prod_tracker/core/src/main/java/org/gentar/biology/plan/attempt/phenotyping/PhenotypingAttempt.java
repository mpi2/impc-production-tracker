package org.gentar.biology.plan.attempt.phenotyping;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
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

    private LocalDateTime phenotypingExperimentsStarted;

    private Boolean doNotCountTowardsCompleteness;

    @NotNull
    // TODO Need to ensure this field is unique in the Java project
    // or restructure the data model. @Column(unique = true) does not
    // work here because early and late phenotyping can be applied to the same colony.
    // @Column(unique = true)
    private String phenotypingExternalRef;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private Strain strain;

}
