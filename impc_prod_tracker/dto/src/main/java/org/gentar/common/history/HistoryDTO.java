package org.gentar.common.history;

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
}
