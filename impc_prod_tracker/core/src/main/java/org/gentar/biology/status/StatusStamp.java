package org.gentar.biology.status;

import java.time.LocalDateTime;

public interface StatusStamp
{
    String getStatusName();
    LocalDateTime getDate();
}
