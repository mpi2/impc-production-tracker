/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.project.privacy;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.gentar.audit.AuditListener;
import org.gentar.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@EntityListeners(AuditListener.class)
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Privacy extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "privacySeq", sequenceName = "PRIVACY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privacySeq")
    private Long id;

    @NotNull
    private String name;
}
