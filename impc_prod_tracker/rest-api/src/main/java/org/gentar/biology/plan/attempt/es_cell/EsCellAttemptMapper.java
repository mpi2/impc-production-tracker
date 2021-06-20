package org.gentar.biology.plan.attempt.es_cell;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.colony.status_stamp.ColonyStatusStamp;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.attempt.esCell.EsCellAttempt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EsCellAttemptMapper implements Mapper<EsCellAttempt, EsCellAttemptDTO>
{
    private final EntityMapper entityMapper;

    public EsCellAttemptMapper (EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }

    @Override
    public EsCellAttemptDTO toDto(EsCellAttempt esCellAttempt) {
        EsCellAttemptDTO esCellAttemptDTO = null;
        if (esCellAttempt != null)
        {
            esCellAttemptDTO = entityMapper.toTarget(esCellAttempt, EsCellAttemptDTO.class);
        }
        return esCellAttemptDTO;
    }

    @Override
    public EsCellAttempt toEntity(EsCellAttemptDTO esCellAttemptDTO) {
        EsCellAttempt esCellAttempt = entityMapper.toTarget(esCellAttemptDTO, EsCellAttempt.class);
        if (Strings.isBlank(esCellAttemptDTO.getComment()))
        {
            esCellAttempt.setComment(null);
        }
        else
        {
            esCellAttempt.setComment(esCellAttemptDTO.getComment());
        }
        if (esCellAttemptDTO.getTotalFemaleChimeras() != null && esCellAttemptDTO.getTotalMaleChimeras() != null)
        {
            Integer femaleChimeras = esCellAttemptDTO.getTotalFemaleChimeras();
            Integer maleChimeras = esCellAttemptDTO.getTotalMaleChimeras();
            Integer total = femaleChimeras + maleChimeras;
            esCellAttempt.setTotalChimeras(total);
        }
        if (esCellAttemptDTO.getCassetteTransmissionVerified() == null)
        {
            LocalDateTime date = null;

            if (esCellAttempt.getPlan().getOutcomes() != null) {
                Outcome outcome = esCellAttempt.getPlan().getOutcomes().stream()
                        .filter(outcome1 -> "Genotype Confirmed".equals(outcome1.getColony().getStatus().getName()))
                        .findFirst()
                        .orElse(null);
                ColonyStatusStamp statusStamp = outcome.getColony().getColonyStatusStamps().stream()
                        .filter(colonyStatusStamp -> "Genotype Confirmed".equals(colonyStatusStamp.getStatusName()))
                        .findFirst()
                        .orElse(null);

                if (statusStamp != null) {
                    date = statusStamp.getDate();
                }

                if (date != null) {
                    esCellAttempt.setCassetteTransmissionVerified(date.toLocalDate());
                    esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(true);
                }
            }
        }
        else {
            esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(false);
        }
        return esCellAttempt;
    }
}
