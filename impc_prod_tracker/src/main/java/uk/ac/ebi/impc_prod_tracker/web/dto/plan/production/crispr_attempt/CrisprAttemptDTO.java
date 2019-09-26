package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CrisprAttemptDTO
{
    @JsonIgnore
    private Long crisprAttemptId;

    @JsonIgnore
    private Long imitsMiAttemptId;

    private LocalDate miDate;

    @JsonProperty("attemptExternalRef")
    private String miExternalRef;

    private Boolean experimental;

    private String comment;
    private String mutagenesisExternalRef;
    private String deliveryTypeMethodName;
    private Double voltage;

    @JsonProperty("numberOfPulses")
    private Integer noOfPulses;

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

    @JsonProperty("totalEmbryosInjected")
    private Integer totalEmbryosInjected;

    @JsonProperty("totalEmbryosSurvived")
    private Integer totalEmbryosSurvived;

    @JsonProperty("embryoTransferDay")
    private String embryoTransferDay;

    @JsonProperty("embryo2Cell")
    private String embryo2Cell;

    @JsonProperty("totalTransferred")
    private Integer totalTransferred;

    private Integer numFounderPups;

    @JsonProperty("assayAttributes")
    private AssayDTO assay;

    private Integer numFounderSelectedForBreeding;

    @JsonProperty("strainInjectedAttributes")
    private StrainDTO strain;
}
