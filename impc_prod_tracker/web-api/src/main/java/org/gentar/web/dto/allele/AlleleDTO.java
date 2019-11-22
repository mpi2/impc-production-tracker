package org.gentar.web.dto.allele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.web.dto.gene.ProjectIntentionGeneDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AlleleDTO
{
    @JsonIgnore
    private Long id;
    private String name;
    private String alleleSymbol;
    private String mgiAlleleId;
    private String alleleTypeName;
    private String alleleSubtypeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("alleleGeneAttributes")
    private List<ProjectIntentionGeneDTO> projectIntentionGeneDTOS;
}
