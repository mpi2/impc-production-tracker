package uk.ac.ebi.impc_prod_tracker.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.GeneRepository;
import java.util.List;

@Component
public class GeneService
{
    private GeneRepository geneRepository;
    private RestTemplate restTemplate;

    @Value("${external_service_url}")
    private String externalServiceUrl;

    private static final String JSON_QUERY_MOUSE_EXTERNAL_REF =
        "{ \"query\":" +
        " \"{ mouse_gene(where: {symbol: {_ilike: \\\"%s\\\"}})" +
        " { symbol mgi_gene_acc_id } }\" }";

    private static final String JSON_QUERY_MOUSE_SYNONYM_EXTERNAL_REF =
        "{ \"query\":" +
            " \"{ mouse_gene_synonym(where: {synonym: {_ilike: \\\"%s\\\"}})" +
            " { symbol mgi_gene_acc_id } }\" }";

    public GeneService(GeneRepository geneRepository, RestTemplate restTemplate) { this.geneRepository = geneRepository;
        this.restTemplate = restTemplate;
    }

    public List<Gene> getGenesBySymbol (String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }

    public Gene getFromExternalGenes(String symbol)
    {
        String payload = String.format(JSON_QUERY_MOUSE_EXTERNAL_REF, symbol);
        ResponseEntity<String> response =
            restTemplate.postForEntity(externalServiceUrl, payload, String.class);
        Gson g = new Gson();
        DataWrapperExternalGene dataWrapper =
            g.fromJson(response.getBody(), DataWrapperExternalGene.class);

        List<ExternalDataMouseGene> externalDataMouseGenes = dataWrapper.getData().getMouseGenes();
        Gene gene = null;
        if (!externalDataMouseGenes.isEmpty())
        {
            gene = new Gene();
            ExternalDataMouseGene firstResult = externalDataMouseGenes.get(0);
            gene.setAccId(firstResult.accId);
            gene.setSymbol(firstResult.symbol);
        }
        return gene;
    }

    public Gene getSynonymFromExternalGenes(String symbol)
    {
        String payload = String.format(JSON_QUERY_MOUSE_SYNONYM_EXTERNAL_REF, symbol);
        ResponseEntity<String> response =
            restTemplate.postForEntity(externalServiceUrl, payload, String.class);
        Gson g = new Gson();
        DataWrapperExternalGene dataWrapper =
            g.fromJson(response.getBody(), DataWrapperExternalGene.class);

        List<ExternalDataMouseGene> externalDataMouseGenes = dataWrapper.getData().getMouseGenes();
        Gene gene = null;
        if (!externalDataMouseGenes.isEmpty())
        {
            gene = new Gene();
            ExternalDataMouseGene firstResult = externalDataMouseGenes.get(0);
            gene.setAccId(firstResult.accId);
            gene.setSymbol(firstResult.symbol);
        }
        return gene;
    }

    public void getFromExternalSynonyms()
    {

    }

    @Data
    static class DataWrapperExternalGene
    {
        private ExternalDataResponse data;
    }

    @Data
    static class ExternalDataResponse
    {
        @SerializedName("mouse_gene")
        List<ExternalDataMouseGene> mouseGenes;
    }

    @Data
    private static class ExternalDataMouseGene
    {
        @SerializedName("mgi_gene_acc_id")
        private String accId;

        private String symbol;
    }
}
