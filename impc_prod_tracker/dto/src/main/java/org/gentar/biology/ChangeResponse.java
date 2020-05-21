package org.gentar.biology;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.common.history.HistoryDTO;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

/**
 * This class contains information about a change in the system. It has a
 * {@link org.gentar.common.history.HistoryDTO} with details about the change and a link to the
 * resource that was updated or created in the system.
 */
@Data
public class ChangeResponse extends RepresentationModel<ChangeResponse>
{
    @JsonUnwrapped
    @JsonProperty("history")
    private List<HistoryDTO> historyDTOs;
}
