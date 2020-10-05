package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.intention.OrthologDTO;
import java.util.List;

@Data
public class ProjectIntentionGeneResponseDTO
{
    @JsonProperty("gene")
    private GeneResponseDTO geneDTO;

    private List<OrthologDTO> bestOrthologs;

    private List<OrthologDTO> allOrthologs;
}
