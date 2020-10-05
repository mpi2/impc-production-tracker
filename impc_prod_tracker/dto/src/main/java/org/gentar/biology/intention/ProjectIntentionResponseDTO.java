package org.gentar.biology.intention;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.gene.ProjectIntentionGeneDTO;
import org.gentar.biology.gene.ProjectIntentionGeneResponseDTO;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;
import java.util.List;

@Data
public class ProjectIntentionResponseDTO
{
    @JsonUnwrapped
    private ProjectIntentionCommonDTO projectIntentionCommonDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionByGene")
    private ProjectIntentionGeneResponseDTO projectIntentionGeneDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionsBySequences")
    private List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS;
}
