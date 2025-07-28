package org.gentar.biology.mutation;


import lombok.Data;

@Data
public class InsertionTargetedExonDTO {

    private Long id;
    private String exonId;
    private String transcript;
}
