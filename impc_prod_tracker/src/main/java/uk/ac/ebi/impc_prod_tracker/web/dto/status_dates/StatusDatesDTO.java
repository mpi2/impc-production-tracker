package uk.ac.ebi.impc_prod_tracker.web.dto.status_dates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class StatusDatesDTO
{
    @JsonIgnore
    private Long id;
    String name;
    LocalDateTime date;
}
