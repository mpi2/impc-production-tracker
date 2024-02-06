package org.gentar.wge;

import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WgeCrisprSearchConsumer
{
    @Value("https://wge.stemcell.sanger.ac.uk//api/crispr_search?exon_id[]=")
    private String CRISPR_SEARCH_URL;

    private final RestTemplate restTemplate;

    public WgeCrisprSearchConsumer(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    public String executeQuery(String query)
    {
        ResponseEntity<String> response;
        try
        {
            response =
                    restTemplate.getForEntity(CRISPR_SEARCH_URL+query, String.class);
        }
        catch(Exception e)
        {
            throw new SystemOperationFailedException(
                    "Invalid exon id.", e.getMessage());
        }
        return processResponseBody(response);
    }

    private String processResponseBody(ResponseEntity<String> response)
    {
        return response.getBody();
    }

}
