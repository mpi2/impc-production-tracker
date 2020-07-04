package org.gentar.common.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDetailDTO
{
    private String field;
    private String oldValue;
    private String newValue;
    private String note;
}
