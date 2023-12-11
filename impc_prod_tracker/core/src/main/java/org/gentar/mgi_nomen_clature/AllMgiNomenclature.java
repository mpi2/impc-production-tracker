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

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class AllMgiNomenclature extends BaseEntity {
    @Id
    @SequenceGenerator(name = "allMgiNomenclatureSeq", sequenceName = "ALL_MGI_NOMENCLATURE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allMgiNomenclatureSeq")
    private Long id;

    String markerMgiId;
    String currentMarkerSymbol;
    String previousMarkerSymbol;
    String event;
    String eventDate;
}
