package org.gentar.biology.ortholog;

import lombok.Data;

@Data
public class Ortholog
{
    private String symbol;
    private String category;
    private String support;
    private Integer supportCount;
    private String link;
}
