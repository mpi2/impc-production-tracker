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
    @Value("http://rest.ensembl.org/map/mus_musculus/")
    private String ENSEMBL_URL;
    private static final String MORE_THAN_ONE_MAPPING = "There are more than one mappings for this guide.";
    private static final String ANY_MAPPING_FOUND = "There are no mappings for this guide.";

    private final RestTemplate restTemplate;

    public AssemblyMapConsumer(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public String executeQuery(String coordinates)
    {
        ResponseEntity<String> response;
        try
        {
            String url = ENSEMBL_URL + "/GRCm38/" + coordinates + "/GRCm39?content-type=application/json";
            response =
                    restTemplate.getForEntity(url, String.class);
        }
        catch(Exception e)
        {
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
