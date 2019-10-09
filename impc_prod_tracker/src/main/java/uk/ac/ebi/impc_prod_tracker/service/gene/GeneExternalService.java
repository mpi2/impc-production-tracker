package uk.ac.ebi.impc_prod_tracker.service.gene;

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
    private static final String JSON_QUERY_MOUSE_EXTERNAL_REF =
        "{ \"query\":" +
            " \"{ mouse_gene(where: {symbol: {_ilike: \\\"%s\\\"}})" +
            " { symbol mgi_gene_acc_id } }\" }";

    private static final String JSON_QUERY_MOUSE_SYNONYM_EXTERNAL_REF =
        "{ \"query\":" +
            " \"{ mouse_gene_synonym(where: {synonym: {_ilike: \\\"%s\\\"}})" +
            " { synonym mgi_gene_acc_id } }\" }";

    public GeneExternalService(GraphQLConsumer graphQLConsumer)
    {
        this.graphQLConsumer = graphQLConsumer;
    }

    public Gene getFromExternalGenes(String symbol)
    {
        String query = String.format(JSON_QUERY_MOUSE_EXTERNAL_REF, symbol);
        String result = graphQLConsumer.executeQuery(query);
        Gene gene = null;
        try
        {
            ExternalDataMouseGeneResponse externalDataResponse =
                JsonHelper.fromJson(result, ExternalDataMouseGeneResponse.class);
            List<ExternalDataMouseGene> externalDataMouseGenes = externalDataResponse.getData();

            if (!externalDataMouseGenes.isEmpty())
            {
                gene = new Gene();
                ExternalDataMouseGene firstResult = externalDataMouseGenes.get(0);
                gene.setAccId(firstResult.accId);
                gene.setSymbol(firstResult.symbol);
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return gene;
    }

    public Gene getSynonymFromExternalGenes(String symbol)
    {
        String query = String.format(JSON_QUERY_MOUSE_SYNONYM_EXTERNAL_REF, symbol);
        String result = graphQLConsumer.executeQuery(query);
        Gene gene = null;
        try
        {
            ExternalDataMouseGeneSynonymResponse externalDataResponse =
                JsonHelper.fromJson(result, ExternalDataMouseGeneSynonymResponse.class);
            List<ExternalDataMouseGeneSynonym> externalDataMouseGeneSynonyms =
                externalDataResponse.getData();

            if (!externalDataMouseGeneSynonyms.isEmpty())
            {
                gene = new Gene();
                ExternalDataMouseGeneSynonym firstResult = externalDataMouseGeneSynonyms.get(0);
                gene.setAccId(firstResult.accId);
                gene.setSymbol(firstResult.synonym);
            }
        }
        catch (IOException e)
        {
            throw new SystemOperationFailedException(e);
        }
        return gene;
    }

    @Data
    static class ExternalDataMouseGeneResponse
    {
        @JsonProperty("mouse_gene")
        List<ExternalDataMouseGene> data;
    }

    @Data
    private static class ExternalDataMouseGene
    {
        @JsonProperty("mgi_gene_acc_id")
        private String accId;

        private String symbol;
    }

    @Data
    static class ExternalDataMouseGeneSynonymResponse
    {
        @JsonProperty("mouse_gene_synonym")
        List<ExternalDataMouseGeneSynonym> data;
    }

    @Data
    private static class ExternalDataMouseGeneSynonym
    {
        @JsonProperty("mgi_gene_acc_id")
        private String accId;

        private String synonym;
    }
}
