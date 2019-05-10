package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.crispr_attempt_details;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CrisprAttemptDetailsDTO
{
    private LocalDateTime miDate;
    private String miExternalRef;
    private Boolean experimental;
    private String comments;
}
