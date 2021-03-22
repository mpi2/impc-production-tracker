package org.gentar.wge;

import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class WgeExonConsumer
{
    @Value("https://wge.stemcell.sanger.ac.uk/api/exon_search?species=Mouse&marker_symbol=")
    private String EXONS_URL;

    private RestTemplate restTemplate;

    public WgeExonConsumer(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public String executeQuery(String query)
    {
        ResponseEntity<String> response;
        try
        {
            response =
                    restTemplate.getForEntity(EXONS_URL+query, String.class);
        }
        catch(Exception e)
        {
            throw new SystemOperationFailedException(
                    "No exons found.", e.getMessage());
        }
        return processResponseBody(response);
    }

    private String processResponseBody(ResponseEntity<String> response)
    {
        String result = response.getBody();
        return result;
    }
}
