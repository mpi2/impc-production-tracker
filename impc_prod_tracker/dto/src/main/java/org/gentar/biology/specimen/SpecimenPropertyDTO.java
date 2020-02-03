package org.gentar.biology.specimen;

import lombok.Data;

@Data
public class SpecimenPropertyDTO
{
    private Long id;
    private String propertyTypeName;
    private String value;
}
