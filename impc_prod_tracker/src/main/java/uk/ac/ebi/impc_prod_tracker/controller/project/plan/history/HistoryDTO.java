package uk.ac.ebi.impc_prod_tracker.controller.project.plan.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HistoryDTO
{
    private int id;
    private String user;
    private LocalDateTime date;
    private String comment;
    private List<HistoryDetailDTO> details;

    @Data
    @AllArgsConstructor
    static class HistoryDetailDTO
    {
        private String field;
        private String oldValue;
        private String newValue;
    }
}
