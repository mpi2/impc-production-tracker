package org.gentar.biology.plan.attempt.crispr.guide.exons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExonDTO
{
    private Long id;

    @JsonProperty("exon_id")
    private String exonId;

    @JsonProperty("guides_sequence_details")
    private List<GuidesSequenceDetailsDTO> guidesSequenceDetailsDTOS;
}
