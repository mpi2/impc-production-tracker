package org.gentar.ensembl_assembly_mapping;

import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.exceptions.UserOperationFailedException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AssemblyMapConsumer
{

    @Value("${liftover.service.url:https://www.gentar.org/liftover-api/liftover}")
    private String LIFT_OVER_URL;
    private static final String MORE_THAN_ONE_MAPPING = "There are more than one mappings for this guide.";
    private static final String ANY_MAPPING_FOUND = "There are no mappings for this guide.";

    private final RestTemplate restTemplate;

    public AssemblyMapConsumer(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }


    public String executeQuery(String chr, Integer start, Integer stop) {
        ResponseEntity<String> response;
        try {
            String url = LIFT_OVER_URL
                    + "?chrom=" + chr
                    + "&start=" + start
                    + "&end=" + stop;

            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            throw new SystemOperationFailedException(
                    "Ensembl assembly mapping failed.", e.getMessage());
        }
        return processResponseBody(response);
    }

    private String processResponseBody(ResponseEntity<String> response)
    {
        return response.getBody();
    }

    public JSONObject validMapping(JSONObject json)
    {
        final JSONObject[] mapped = {null};
        if (!json.getJSONArray("mappings").isEmpty())
        {
            if (json.getJSONArray("mappings").length() == 1)
            {
                json.getJSONArray("mappings").forEach(item -> mapped[0] = (JSONObject) item);
                return mapped[0];
            } else {
                throw new UserOperationFailedException(MORE_THAN_ONE_MAPPING);
            }
        } else {
            throw new UserOperationFailedException(ANY_MAPPING_FOUND);
        }
    }
}
