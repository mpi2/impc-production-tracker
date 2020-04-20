package org.gentar.common.history;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HistoryDTO
{
    private int id;
    private String user;
    private LocalDate date;
    private String action;
    private String comment;
    private List<HistoryDetailDTO> details;
}
