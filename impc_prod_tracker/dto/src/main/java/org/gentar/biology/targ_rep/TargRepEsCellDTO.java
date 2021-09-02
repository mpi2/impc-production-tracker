package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TargRepEsCellDTO
{
    @JsonIgnore
    private Long id;

    private String name;
}
