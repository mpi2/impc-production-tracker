package uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CrisprAttemptDTO
{
    @JsonIgnore
    private Long imitsMiAttemptId;

    @JsonProperty("mi_date")
    private LocalDateTime miDate;

    @JsonProperty("attempt_external_ref")
    private String miExternalRef;

    private Boolean experimental;

    private String comment;

    @JsonProperty("mutagenesis_external_ref")
    private String mutagenesisExternalRef;

    @JsonProperty("delivery_type_method_name")
    private String deliveryTypeMethodName;

    private Integer voltage;

    @JsonProperty("number_of_pulses")
    private Integer numberOfPulses;

    @JsonProperty("nucleases_attributes")
    private List<NucleaseDTO> nucleaseDTOS;

    @JsonProperty("guides_attributes")
    private List<GuideDTO> guideDTOS;

    @JsonProperty("mutagenesis_donors_attributes")
    private List<MutagenesisDonorDTO> mutagenesisDonorDTOS;

    @JsonProperty("reagents_attributes")
    private List<ReagentDTO> reagentDTOS;

    @JsonProperty("genotype_primers_attributes")
    private List<GenotypePrimerDTO> genotypePrimerDTOS;

    private Integer total_embryos_injected;
    private Integer total_embryos_survived;
    private String embryo_transfer_day;
    private Integer embryo_2_cell;
    private Integer total_transferred;
    private Integer num_founder_pups;

    @JsonProperty("assay_attributes")
    private AssayDTO assayDTO;

    @JsonProperty("num_founders_selected_for_breeding")
    private Integer numFoundersSelectedForBreeding;

    @JsonProperty("strain_injected_attributes")
    private StrainDTO strainDTO;
}
