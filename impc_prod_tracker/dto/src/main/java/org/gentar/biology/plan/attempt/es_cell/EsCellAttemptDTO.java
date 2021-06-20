package org.gentar.biology.plan.attempt.es_cell;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class EsCellAttemptDTO
{
    @JsonIgnore
    private Long esCellAttemptId;

    private String esCellName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsMiAttemptId")
    private Long imitsMiAttempt;

    private LocalDate miDate;

    @JsonProperty("attemptExternalRef")
    private String miExternalRef;

    private Boolean experimental;

    private String comment;

    private String blastStrainName;

    private Integer totalBlastsInjected;

    private Integer totalTransferred;

    private Integer numberSurrogatesReceiving;

    private Integer totalPupsBorn;

    private Integer totalFemaleChimeras;

    private Integer totalMaleChimeras;

    private Integer numberOfMalesWith0To39PercentChimerism;

    private Integer numberOfMalesWith40To79PercentChimerism;

    private Integer numberOfMalesWith80To99PercentChimerism;

    private Integer numberOfMalesWith100PercentChimerism;

    private String testCrossStrainName;

    private LocalDate dateChimerasMated;

    private Integer numberOfChimeraMatingsAttempted;

    private Integer numberOfChimeraMatingsSuccessful;

    private Integer numberOfChimerasWithGltFromCct;

    private Integer numberOfChimerasWithGltFromGenotyping;

    private Integer numberOfChimerasWith0To9PercentGlt;

    private Integer numberOfChimerasWith10To49PercentGlt;

    private Integer numberOfChimerasWith50To99PercentGlt;

    private Integer numberOfChimerasWith100PercentGlt;

    private Integer totalF1MiceFromMatings;

    private Integer numberOfCctOffspring;

    private LocalDate cassetteTransmissionVerified;

    private Integer numberOfHetOffspring;

    private Integer numberOfLiveGltOffspring;
}
