package uk.ac.ebi.impc_prod_tracker.web.dto.intention;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.web.dto.allele_categorization.AlleleCategorizationDTO;

import java.util.List;

@Data
public class ProjectIntentionDTO
{
    private String alleleTypeName;
    private String molecularMutationTypeName;

    @JsonProperty("alleleCategorizations")
    private List<AlleleCategorizationDTO> alleleCategorizationDTOS;
}
