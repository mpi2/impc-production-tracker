package org.gentar.biology.ortholog;

import lombok.Data;

import java.util.List;

@Data
public class Ortholog
{
    private String symbol;
    private String category;
    private String support;
    private Integer supportCount;
}
