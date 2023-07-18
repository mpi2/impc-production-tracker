package org.gentar.biology.glt_production_numbers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GltAttemptProjectionDto {

    @JsonProperty("year")
    private Long year;

    @JsonProperty("month")
    private Long month;

    @JsonProperty("workUnitName")
    private String workUnitName;

    @JsonProperty("workGroupName")
    private String workGroupName;

    @JsonProperty("sum")
    private Long sum;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("count")
    private Long count;
}
