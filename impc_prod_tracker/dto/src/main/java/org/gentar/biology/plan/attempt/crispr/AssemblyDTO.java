package org.gentar.biology.plan.attempt.crispr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AssemblyDTO
{
    private Integer start;
    private String chr;
    private String assembly;
    private Integer stop;
    private String strand;
}
