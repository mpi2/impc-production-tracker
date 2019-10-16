package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.ProjectIntentionDTO;

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
