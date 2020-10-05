package org.gentar.biology.intention;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.gene.ProjectIntentionGeneCreationDTO;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;

import java.util.List;

@Data
public class ProjectIntentionCreationDTO
{
    @JsonUnwrapped
    private ProjectIntentionCommonDTO projectIntentionCommonDTO;

    @JsonProperty("intentionByGene")
    private ProjectIntentionGeneCreationDTO projectIntentionGeneCreationDTO;

    @JsonProperty("intentionsBySequences")
    private List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTO;
}
