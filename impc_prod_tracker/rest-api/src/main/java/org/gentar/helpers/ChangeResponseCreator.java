package org.gentar.helpers;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.common.history.HistoryDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class ChangeResponseCreator
{
    private final HistoryMapper historyMapper;

    public ChangeResponseCreator(HistoryMapper historyMapper)
    {
        this.historyMapper = historyMapper;
    }

    public ChangeResponse create(Link link, History historyRecord)
    {
        return create(link, Collections.singletonList(historyRecord));
    }

    public ChangeResponse create(Link link, Collection<History> historyRecords)
    {
        ChangeResponse changeResponse = new ChangeResponse();
        changeResponse.add(link);
        List<HistoryDTO> historyDTOS = historyMapper.toDtos(historyRecords);
        changeResponse.setHistoryDTOs(historyDTOS);
        return changeResponse;
    }
}
