package uk.ac.ebi.impc_prod_tracker.web.dto.gene;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeneDTO
{
    private String accessionIdValue;
    private String symbol;
    private String specieName;
}
