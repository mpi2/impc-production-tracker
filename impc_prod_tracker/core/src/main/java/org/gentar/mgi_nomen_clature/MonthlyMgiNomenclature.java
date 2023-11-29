package org.gentar.mgi_nomen_clature;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.gene.flag.GeneFlag;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.species.Species;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class MonthlyMgiNomenclature extends BaseEntity {
    @Id
    @SequenceGenerator(name = "monthlyMgiNomenclatureSeq", sequenceName = "MONTHLY_MGI_NOMENCLATURE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monthlyMgiNomenclatureSeq")
    private Long id;

    String markerMgiId;
    String currentMarkerSymbol;
    String previousMarkerSymbol;
    String event;
    String eventDate;
}
