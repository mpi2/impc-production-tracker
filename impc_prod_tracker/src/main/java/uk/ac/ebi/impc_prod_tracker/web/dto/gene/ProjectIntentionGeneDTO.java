package uk.ac.ebi.impc_prod_tracker.web.dto.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.intention.ProjectIntentionDTO;

@Data
@RequiredArgsConstructor
public class ProjectIntentionGeneDTO
{
    @JsonProperty("gene")
    private GeneDTO geneDTO;

    @JsonProperty("intention")
    private ProjectIntentionDTO projectIntentionDTO;
}
