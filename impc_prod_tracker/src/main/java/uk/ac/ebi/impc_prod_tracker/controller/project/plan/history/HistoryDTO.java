package uk.ac.ebi.impc_prod_tracker.controller.project.plan.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDTO
{
    private int id;
    private String user;
    private LocalDateTime date;
    private String action;
}
