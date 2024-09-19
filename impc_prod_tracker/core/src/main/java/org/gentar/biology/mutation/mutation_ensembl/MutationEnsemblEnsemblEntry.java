package org.gentar.biology.mutation.mutation_ensembl;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MutationEnsemblEnsemblEntry {
    @JsonProperty("mouse_gene")
    List<MutationEnsemblEnsemblPartDto> mutationEnsemblEnsemblPartDtos;
}
