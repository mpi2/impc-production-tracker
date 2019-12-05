/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.gene.external_ref;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.graphql.GraphQLConsumer;
import org.gentar.graphql.QueryBuilder;
import org.gentar.util.JsonHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene.Gene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeneExternalService
{
    private GraphQLConsumer graphQLConsumer;
    
    public GeneExternalService(GraphQLConsumer graphQLConsumer)
    {
        this.graphQLConsumer = graphQLConsumer;
    }

    @Cacheable("external_genes_by_input")
    public Gene getGeneFromExternalDataBySymbolOrAccId(String input)
    {
        String query =
            String.format(ExternalReferenceConstants.GENE_BY_SYMBOL_OR_ACC_ID_QUERY, input, input);
        return getGeneFromExternalData(query);
    }

    public Map<String, String> getAccIdsByMarkerSymbols(List<String> inputs)
    {
        if (inputs.size() <= 1)
        {
            return getAccIdsBySingleMarkerSymbol(inputs.get(0));
        }
        else
        {
            return getAccIdsByCollectionMarkerSymbols(inputs);
        }
    }

    private Map<String, String> getAccIdsBySingleMarkerSymbol(String input)
    {
        String query = QueryBuilder.getInstance()
            .withRoot("mouse_gene")
            .withColumnInLikeValuesIgnoreCase("symbol", Arrays.asList(input))
            .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
            .build();
        return getAccIdsFromExternalData(query);
    }

    private Map<String, String> getAccIdsByCollectionMarkerSymbols(List<String> inputs)
    {
        Map<String, String> accIds = new LinkedHashMap<>();
        String query = QueryBuilder.getInstance()
            .withRoot("mouse_gene")
            .withColumnInLikeValuesIgnoreCase("symbol", inputs)
            .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
            .build();
        var queryResults = getAccIdsFromExternalData(query);
        inputs.forEach(x ->
        {
            accIds.put(x, queryResults.get(x.toLowerCase()));
        });
        return accIds;
    }

    public List<Gene> getGenesFromExternalDataBySymbolOrAccId(String input)
    {
        String query =
            String.format(ExternalReferenceConstants.GENE_BY_SYMBOL_OR_ACC_ID_QUERY, input, input);
        return getGenesFromExternalData(query);
    }

    public Gene getSynonymFromExternalGenes(String symbol)
    {
        String query = String.format(ExternalReferenceConstants.SYNONYM_BY_SYMBOL_QUERY, symbol);
        return getGeneFromExternalData(query);
    }

    private Gene getGeneFromExternalData(String query)
    {
        Gene gene = null;
        List<Gene> genes = getGenesFromExternalData(query);
        if (!genes.isEmpty())
        {
            gene = genes.get(0);
        }
        return gene;
    }

    private List<Gene> getGenesFromExternalData(String query)
    {
        String result = graphQLConsumer.executeQuery(query);
        List<Gene> genes = new ArrayList<>();
        try
        {
            MouseGeneResponse externalDataResponse =
                JsonHelper.fromJson(result, MouseGeneResponse.class);
            var externalDataMouseGenes = externalDataResponse.getData();

            if (!externalDataMouseGenes.isEmpty())
            {
                externalDataMouseGenes.forEach(x -> genes.add(getGene(x)));
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return genes;
    }

    private Map<String, String> getAccIdsFromExternalData(String query)
    {
        String result = graphQLConsumer.executeQuery(query);
        Map<String, String> accIds = new HashMap<>();
        try
        {
            MouseGeneResponse externalDataResponse =
                JsonHelper.fromJson(result, MouseGeneResponse.class);
            var externalDataMouseGenes = externalDataResponse.getData();

            if (externalDataMouseGenes != null)
            {
                externalDataMouseGenes.forEach(
                    x -> accIds.put(x.getSymbol().toLowerCase(), x.getAccId()));
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return accIds;
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
