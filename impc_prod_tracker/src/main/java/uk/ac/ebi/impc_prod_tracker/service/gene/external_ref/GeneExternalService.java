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
package uk.ac.ebi.impc_prod_tracker.service.gene.external_ref;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.json.JsonHelper;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.SystemOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.service.externalData.GraphQLConsumer;
import java.io.IOException;
import java.util.List;

@Component
public class GeneExternalService
{
    private GraphQLConsumer graphQLConsumer;
    
    public GeneExternalService(GraphQLConsumer graphQLConsumer)
    {
        this.graphQLConsumer = graphQLConsumer;
    }

    public Gene getFromExternalGenesBySymbolOrAccId(String input)
    {
        String query =
            String.format(ExternalReferenceConstants.GENE_BY_SYMBOL_OR_ACC_ID_QUERY, input, input);
        return getGeneFromExternalData(query);
    }

    public Gene getSynonymFromExternalGenes(String symbol)
    {
        String query = String.format(ExternalReferenceConstants.SYNONYM_BY_SYMBOL_QUERY, symbol);
        return getGeneFromExternalData(query);
    }

    private Gene getGeneFromExternalData(String query)
    {
        String result = graphQLConsumer.executeQuery(query);
        Gene gene = null;
        try
        {
            MouseGeneResponse externalDataResponse =
                JsonHelper.fromJson(result, MouseGeneResponse.class);
            List<MouseGeneExternalReferenceDTO> externalDataMouseGenes = externalDataResponse.getData();

            if (!externalDataMouseGenes.isEmpty())
            {
                gene = getGene(externalDataMouseGenes.get(0));
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return gene;
    }

    private Gene getGene(MouseGeneExternalReferenceDTO mouseGene)
    {
        Gene gene;
        gene = new Gene();
        gene.setAccId(mouseGene.getAccId());
        gene.setSymbol(mouseGene.getSymbol());
        gene.setName(mouseGene.getName());
        gene.setType(mouseGene.getType());
        gene.setGenomeBuild(mouseGene.getGenomeBuild());
        gene.setEntrezGeneId(mouseGene.getEntrezGeneId());
        gene.setNcbiChromosome(mouseGene.getNcbiChromosome());
        gene.setNcbiStart(mouseGene.getNcbiStart());
        gene.setNcbiStop(mouseGene.getNcbiStop());
        gene.setNcbiStrand(mouseGene.getNcbiStrand());
        gene.setEnsemblGeneId(mouseGene.getEnsemblGeneId());
        gene.setEnsemblChromosome(mouseGene.getEnsemblChromosome());
        gene.setEnsemblStart(mouseGene.getEnsemblStart());
        gene.setEnsemblStop(mouseGene.getEnsemblStop());
        gene.setEnsemblStrand(mouseGene.getEnsemblStrand());
        return gene;
    }

    @Data
    static class MouseGeneResponse
    {
        @JsonProperty("mouse_gene")
        List<MouseGeneExternalReferenceDTO> data;
    }
}
