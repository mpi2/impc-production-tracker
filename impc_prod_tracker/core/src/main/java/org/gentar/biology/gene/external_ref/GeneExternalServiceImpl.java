package org.gentar.biology.gene.external_ref;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;
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
public class GeneExternalServiceImpl implements GeneExternalService
{
    private final GraphQLConsumer graphQLConsumer;
    private static final int ELEMENTS_BY_REQUEST = 1000;

    public GeneExternalServiceImpl(GraphQLConsumer graphQLConsumer)
    {
        this.graphQLConsumer = graphQLConsumer;
    }

    @Override
    @Cacheable("external_genes_by_input")
    public Gene getGeneFromExternalDataBySymbolOrAccId(String input)
    {
        String query =
            String.format(ExternalReferenceConstants.GENE_BY_SYMBOL_OR_ACC_ID_QUERY, input, input);
        return getGeneFromExternalData(query);
    }

    @Override
    public Map<String, String> getAccIdsBySymbolsBulk(List<String> symbols)
    {
        Map<String, String> result = new HashMap<>();
        List<List<String>> subLists = ListUtils.partition(symbols, ELEMENTS_BY_REQUEST);
        for (List<String> subList : subLists)
        {
            Map<String, String> subListMap = getAccIdsBySymbols(subList);
            subListMap.forEach(
                (key, value) -> result.merge(key, value, (v1, v2) -> v2));
        }
        return result;
    }

    @Override
    public Map<String, String> getAccIdsByMarkerSymbols(List<String> inputs)
    {
        return getAccIdsByCollectionMarkerSymbols(inputs);
    }

    @Override
    public Map<String, String> getSymbolsByAccessionIdsBulk(List<String> accIds)
    {
        Map<String, String> result = new HashMap<>();
        List<List<String>> subLists = ListUtils.partition(accIds, ELEMENTS_BY_REQUEST);
        for (List<String> subList : subLists)
        {
            Map<String, String> subListMap = getSymbolsByAccessionIds(subList);
            subListMap.forEach(
                (key, value) -> result.merge(key, value, (v1, v2) -> v2));
        }
        return result;
    }


    public Map<String, String> getAccIdsBySymbols(List<String> symbols)
    {
        List<String> conditions = new ArrayList<>();
        conditions.add(QueryBuilder.getColumnInExactMatchCondition("symbol", symbols));
        conditions.add(QueryBuilder.getColumnInExactMatchCondition("mgi_gene_acc_id", symbols));

        String query = QueryBuilder.getInstance()
            .withRoot("mouse_gene")
            .withOrCondition(conditions)
            .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
            .build();
        return getAccIdsFromExternalData(query);
    }

    @Override
    public Map<String, String> getSymbolsByAccessionIds(List<String> accessionIds)
    {
        String query = QueryBuilder.getInstance()
            .withRoot("mouse_gene")
            .withColumnInExactMatch("mgi_gene_acc_id", accessionIds)
            .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
            .build();
        return getSymbolsFromExternalData(query);
    }

    // Not Used Method
//    private Map<String, String> getAccIdsBySingleMarkerSymbol(String input)
//    {
//        String query = QueryBuilder.getInstance()
//                .withRoot("mouse_gene")
//                .withColumnInLikeValuesIgnoreCase("symbol", Arrays.asList(input))
//                .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
//                .build();
//        return getAccIdsFromExternalData(query);
//    }

    private Map<String, String> getAccIdsByCollectionMarkerSymbols(List<String> inputs)
    {
        Map<String, String> accIds = new LinkedHashMap<>();
        String query = QueryBuilder.getInstance()
                .withRoot("mouse_gene")
                .withColumnInExactMatch("mgi_gene_acc_id", inputs)
                .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
                .build();
        var queryResults = getAccIdsFromExternalData(query);
        inputs.forEach(x ->
        {
            accIds.put(x, queryResults.get(x.toLowerCase()));
        });
        return accIds;
    }

    @Override
    public List<Gene> getGenesFromExternalDataBySymbolOrAccId(String input)
    {
        String query =
                String.format(ExternalReferenceConstants.GENE_BY_SYMBOL_OR_ACC_ID_QUERY, input, input);
        return getGenesFromExternalData(query);
    }

    @Override
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

    private Map<String, String> getSymbolsFromExternalData(String query)
    {
        String result = graphQLConsumer.executeQuery(query);
        Map<String, String> symbolsByAccIds = new HashMap<>();
        try
        {
            MouseGeneResponse externalDataResponse =
                JsonHelper.fromJson(result, MouseGeneResponse.class);
            var externalDataMouseGenes = externalDataResponse.getData();

            if (externalDataMouseGenes != null)
            {
                externalDataMouseGenes.forEach(
                    x -> symbolsByAccIds.put(x.getAccId(), x.getSymbol()));
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return symbolsByAccIds;
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
        gene.setMgiCm(mouseGene.getMgiCm());
        gene.setMgiChromosome(mouseGene.getMgiChromosome());
        gene.setMgiStart(mouseGene.getMgiStart());
        gene.setMgiStop(mouseGene.getMgiStop());
        gene.setMgiStrand(mouseGene.getMgiStrand());
        return gene;
    }

    @Data
    static class MouseGeneResponse
    {
        @JsonProperty("mouse_gene")
        List<MouseGeneExternalReferenceDTO> data;
    }
}
