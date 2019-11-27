package org.gentar.common.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterDTO
{
    private String name;
    private List<String> values;
}
