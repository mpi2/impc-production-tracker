package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.allele_categorization.AlleleCategorizationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.intention.ProjectIntentionDTO;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ProjectIntentionSequenceDTO
{

    @JsonProperty("sequenceAttributes")
    private SequenceDTO sequenceDTO;

    @JsonProperty("intention")
    private ProjectIntentionDTO projectIntentionDTO;

    private Integer index;
}
