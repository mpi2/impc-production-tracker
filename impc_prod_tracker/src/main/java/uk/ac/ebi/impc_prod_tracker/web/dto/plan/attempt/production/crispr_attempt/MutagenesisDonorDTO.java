package uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MutagenesisDonorDTO extends RepresentationModel
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long crispr_attempt_plan_id;
    private String vectorName;
    private Double concentration;
    private String preparation;
    private String oligoSequenceFa;
}
