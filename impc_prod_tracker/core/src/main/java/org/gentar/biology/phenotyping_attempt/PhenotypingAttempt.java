package org.gentar.biology.phenotyping_attempt;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;

import javax.persistence.*;
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

    private Boolean phenotypingStarted;

    private Boolean phenotypingComplete;

    private Boolean doNotCountTowardsCompleteness;

    private String phenotypingColonyName;
}
