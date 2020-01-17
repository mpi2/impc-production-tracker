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
package org.gentar.biology.mutation.annotation;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutationAnnotation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mutationAnnotationSeq", sequenceName = "MUTATION_ANNOTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationAnnotationSeq")
    private Long id;

    @NotNull
    @OneToOne(targetEntity = Mutation.class)
    private Mutation mutation;

    @NotNull
    private String chr;

    @NotNull
    private Long start;

    @NotNull
    private Long stop;

    private String refSeq;

    private String altSeq;

    private String exdels;

    private String partialExdels;

    private String txc;

    private Boolean spliceDonor;

    private Boolean spliceAcceptor;

    private Boolean proteinCodingRegion;

    private Boolean intronic;

    private Boolean frameShift;

    private String linkedConsequence;

    private Boolean downstreamOfStop;

    private Boolean stopGained;

    private String aminoAcid;

    private String dupCoords;

    private String consequence;
}
