/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.colony;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.colony.distribution.DistributionProduct;
import org.gentar.biology.colony.status_stamp.ColonyStatusStamp;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.status.Status;
import org.gentar.biology.strain.Strain;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name", "legacy_modification"})
})
public class Colony extends BaseEntity implements ProcessData
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Outcome outcome;

    @NotNull
    private String name;

    @Column(name = "legacy_modification", columnDefinition = "boolean default false")
    private Boolean legacyModification;

    @Column(name = "legacy_without_sequence", columnDefinition = "boolean default false")
    private Boolean legacyWithoutSequence;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private Strain strain;

    @NotNull
    @ManyToOne
    private Status status;

    @Column(columnDefinition="TEXT")
    private String genotypingComment;

    @IgnoreForAuditingChanges
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "colony")
    private Set<ColonyStatusStamp> colonyStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "colony", orphanRemoval=true)
    private Set<DistributionProduct> distributionProducts;

    // Copy Constructor
    public Colony(Colony colony)
    {
        this.id = colony.id;
        this.outcome = colony.outcome;
        this.name = colony.name;
        this.legacyModification = colony.legacyModification;
        this.legacyWithoutSequence = colony.legacyWithoutSequence;
        this.strain = colony.strain;
        this.status = colony.status;
        this.genotypingComment = colony.genotypingComment;
        this.colonyStatusStamps =
            colony.colonyStatusStamps == null ? null : new HashSet<>(colony.colonyStatusStamps);
        this.distributionProducts =
            colony.distributionProducts == null ? null : new HashSet<>(colony.distributionProducts);
    }

    private transient ProcessEvent event;

    @Override
    public ProcessEvent getProcessDataEvent() {
        return this.event;
    }

    @Override
    public void setProcessDataEvent(ProcessEvent processEvent) {
        this.event=processEvent;
    }

    @Override
    public Status getProcessDataStatus() {
        return this.status;
    }

    @Override
    public void setProcessDataStatus(Status status) {
        this.status=status;
    }
}
