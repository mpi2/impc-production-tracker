package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectLocationDTO;
import java.util.List;

@Data
public class ProjectSummaryDTO extends RepresentationModel
{
    private String tpn;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("status")
    private String assigmentStatusName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intention_by_gene_attributes")
    private List<ProjectGeneDTO> projectGeneDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intention_by_location_attributes")
    private List<ProjectLocationDTO> projectLocationDTOS;

    @JsonProperty("is_active")
    private Boolean isActive;
}

