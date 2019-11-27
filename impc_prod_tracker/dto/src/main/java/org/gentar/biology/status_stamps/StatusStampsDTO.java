package org.gentar.biology.status_stamps;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class StatusStampsDTO
{
    private String statusName;
    private LocalDateTime date;
}
