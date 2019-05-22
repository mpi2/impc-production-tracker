package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt;

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
    private Double concentration;
    private String preparation;
    private String oligoSequence;
}
