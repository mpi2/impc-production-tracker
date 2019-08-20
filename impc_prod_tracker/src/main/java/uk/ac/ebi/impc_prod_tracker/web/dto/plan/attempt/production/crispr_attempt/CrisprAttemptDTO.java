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
    private Long planId;
    @JsonIgnore
    private Long imitsMiAttemptId;
    private LocalDateTime miDate;
    private String attemptExternalRef;
    private Boolean experimental;
    private String comment;
    private String mutagenesisExternalRef;
    private String deliveryTypeMethodName;
    private Integer voltage;
    private Integer numberOfPulses;

    @JsonProperty("nucleasesAttributes")
    private List<NucleaseDTO> nucleaseDTOS;

    @JsonProperty("guidesAttributes")
    private List<GuideDTO> guideDTOS;

    @JsonProperty("mutagenesisDonorsAttributes")
    private List<MutagenesisDonorDTO> mutagenesisDonorDTOS;

    @JsonProperty("reagentsAttributes")
    private List<ReagentDTO> reagentDTOS;

    @JsonProperty("genotypePrimersAttributes")
    private List<GenotypePrimerDTO> genotypePrimerDTOS;

    private Integer total_embryos_injected;
    private Integer total_embryos_survived;
    private String embryo_transfer_day;
    private Integer embryo_2_cell;
    private Integer total_transferred;
    private Integer num_founder_pups;

    @JsonProperty("assayAttributes")
    private AssayDTO assayDTO;

    private Integer numFoundersSelectedForBreeding;

    @JsonProperty("strainAttributes")
    private StrainDTO strainDTO;
}
