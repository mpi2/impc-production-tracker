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
package org.gentar.biology.plan.protocol.version;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.protocol.Protocol;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProtocolVersion extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "protocolVersionSeq", sequenceName = "PROTOCOL_VERSION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "protocolVersionSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Protocol.class)
    private Protocol protocol;

    private String protocolUrl;

    private Integer version;
}
