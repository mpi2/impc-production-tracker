package org.gentar.biology.ortholog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.util.JsonHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transforms a JSON with Orthologs information to a map of Ortholog objects.
 */
@Service
public class JSONToOrthologsMapper
{
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String HGNC_URL = "https://www.genenames.org/data/gene-symbol-report/#!/hgnc_id/";

    Map<String, List<Ortholog>> toOrthologs(String json)
    {
        Map<String, List<Ortholog>> orthologs = new HashMap<>();
        try
        {
            JsonNode root = mapper.readTree(json);
            for (JsonNode node : root)
            {
                for(JsonNode child : node)
                {
                    String resultAsText = child.toString();
                    addOrthologs(resultAsText, orthologs);
                }
            }
        }
        catch (JsonProcessingException e)
        {
            throw new SystemOperationFailedException(e.getMessage(), e.getOriginalMessage());
        }
        return orthologs;
    }

    Map<String, List<Ortholog>> addOrthologs(
        String result, Map<String, List<Ortholog>> orthologsMap)
    {
        List<Ortholog> orthologs = new ArrayList<>();
        try
        {
            var mappedResult = JsonHelper.fromJson(result, QueryResult.class);
            if (mappedResult != null && mappedResult.orthologResults != null)
            {
                mappedResult.orthologResults.forEach(x -> {
                    Ortholog ortholog = new Ortholog();
                    ortholog.setSymbol(x.getHumanGeneResult().getSymbol());
                    ortholog.setLink(HGNC_URL + x.getHumanGeneResult().getHgncAccId());
                    ortholog.setCategory(x.getCategory());
                    ortholog.setSupport(x.getSupport());
                    ortholog.setSupportCount(x.getSupportCount());
                    orthologs.add(ortholog);
                });
            }
            orthologsMap.put(mappedResult.mgiGeneAccId, orthologs);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return orthologsMap;
    }


    @Data
    static class QueryResult
    {
        @JsonProperty("mgi_gene_acc_id")
        private String mgiGeneAccId;

        private String symbol;

        @JsonProperty("orthologs")
        private List<OrthologResult> orthologResults;
    }

    @Data
    static class OrthologResult
    {
        @JsonProperty("human_gene")
        private HumanGeneResult humanGeneResult;

        private String category;

        private String support;

        @JsonProperty("support_count")
        private Integer supportCount;
    }

    @Data
    static class HumanGeneResult
    {
        private String symbol;

        @JsonProperty("hgnc_acc_id")
        private String hgncAccId;
    }
}
