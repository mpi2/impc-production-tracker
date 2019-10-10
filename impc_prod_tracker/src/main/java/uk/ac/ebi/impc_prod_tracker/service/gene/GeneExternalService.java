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
            " \"{" +
            "  mouse_gene(where: {mouse_gene_synonym_relations: {mouse_gene_synonym: {synonym: {_ilike: \\\"%s\\\"}}}}) {" +
            "    symbol" +
            "    mgi_gene_acc_id" +
            "  }" +
            "}\" " +
            "}";

    public GeneExternalService(GraphQLConsumer graphQLConsumer)
    {
        this.graphQLConsumer = graphQLConsumer;
    }

    public Gene getFromExternalGenes(String symbol)
    {
        String query = String.format(JSON_QUERY_MOUSE_EXTERNAL_REF, symbol);
        return getGeneFromExternalData(query);
    }

    public Gene getSynonymFromExternalGenes(String symbol)
    {
        String query = String.format(JSON_QUERY_MOUSE_SYNONYM_EXTERNAL_REF, symbol);
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
            List<MouseGene> externalDataMouseGenes = externalDataResponse.getData();

            if (!externalDataMouseGenes.isEmpty())
            {
                gene = new Gene();
                MouseGene firstResult = externalDataMouseGenes.get(0);
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

    @Data
    static class MouseGeneResponse
    {
        @JsonProperty("mouse_gene")
        List<MouseGene> data;
    }

    @Data
    private static class MouseGene
    {
        @JsonProperty("mgi_gene_acc_id")
        private String accId;

        private String symbol;
    }
}
