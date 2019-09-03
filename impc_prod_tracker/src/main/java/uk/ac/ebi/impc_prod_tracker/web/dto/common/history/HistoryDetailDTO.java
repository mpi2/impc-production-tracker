package uk.ac.ebi.impc_prod_tracker.web.dto.common.history;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryDetailDTO
{
    private String field;
    private String oldValue;
    private String newValue;
}
