package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProjectRequestDTO implements Serializable {
    @NotNull private String workUnit;
    @NotNull private String workGroup;
    @NotNull private Set<LocationDTO> locations;
    @NotNull private Set<GenesDTO> genes;

    @Data
    public static class LocationDTO
    {
        private String backgroundStrain;
        private Integer chr;
        private String genomeBuild;
        private Integer index;
        private String intention;
        private String sequence;
        private String specie;
        private Integer start;
        private Integer stop;
        private String strand;
    }

    @Data
    public static class GenesDTO
    {
        private String symbol;
        private String mgiId;
        private String intention;
    }
}
