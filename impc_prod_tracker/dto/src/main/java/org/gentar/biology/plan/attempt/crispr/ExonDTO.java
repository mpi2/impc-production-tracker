package org.gentar.biology.plan.attempt.crispr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ExonDTO {
    private Long id;
    private String exonId;

    @JsonProperty("guideDetails")
    private List<GuideDTO> guideDetailsDTOS;
}
