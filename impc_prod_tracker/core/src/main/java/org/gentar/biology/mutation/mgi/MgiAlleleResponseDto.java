package org.gentar.biology.mutation.mgi;

import java.util.List;
import lombok.Data;


@Data
public class MgiAlleleResponseDto {

    List<MgiAlleleDto> duplicateMutation;

    List<MgiAlleleDto> noMutation;

    List<MgiAlleleDto> noUpdateNecessary;

    List<MgiAlleleDto> updated;




}
