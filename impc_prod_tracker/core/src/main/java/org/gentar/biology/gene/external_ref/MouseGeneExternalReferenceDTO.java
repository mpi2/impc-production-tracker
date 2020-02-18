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
package org.gentar.biology.gene.external_ref;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Class with the structure of the data retrieved by the external reference service for a mouse
 * gene record.
 */
@Data
class MouseGeneExternalReferenceDTO
{
    @JsonProperty("mgi_gene_acc_id")
    private String accId;

    private String symbol;

    private String name;

    private String type;

    @JsonProperty("genome_build")
    private String genomeBuild;

    @JsonProperty("entrez_gene_acc_id")
    private Long entrezGeneId;

    @JsonProperty("ncbi_chromosome")
    private String ncbiChromosome;

    @JsonProperty("ncbi_start")
    private Long ncbiStart;

    @JsonProperty("ncbi_stop")
    private Long ncbiStop;

    @JsonProperty("ncbi_strand")
    private String ncbiStrand;

    @JsonProperty("ensembl_gene_acc_id")
    private String ensemblGeneId;

    @JsonProperty("ensembl_chromosome")
    private String ensemblChromosome;

    @JsonProperty("ensembl_start")
    private Long ensemblStart;

    @JsonProperty("ensembl_stop")
    private Long ensemblStop;

    @JsonProperty("ensembl_strand")
    private String ensemblStrand;

    @JsonProperty("mgi_cm")
    private String mgiCm;

    @JsonProperty("mgi_chromosome")
    private String mgiChromosome;

    @JsonProperty("mgi_start")
    private Long mgiStart;

    @JsonProperty("mgi_stop")
    private Long mgiStop;

    @JsonProperty("mgi_strand")
    private String mgiStrand;
}
