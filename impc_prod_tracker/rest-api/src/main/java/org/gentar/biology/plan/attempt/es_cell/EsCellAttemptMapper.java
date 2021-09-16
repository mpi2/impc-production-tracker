package org.gentar.biology.plan.attempt.es_cell;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.colony.status_stamp.ColonyStatusStamp;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EsCellAttemptMapper implements Mapper<EsCellAttempt, EsCellAttemptDTO>
{
    private final EntityMapper entityMapper;
    private final TargRepEsCellService targRepEsCellService;

    public EsCellAttemptMapper(EntityMapper entityMapper, TargRepEsCellService targRepEsCellService) {
        this.entityMapper = entityMapper;
        this.targRepEsCellService = targRepEsCellService;
    }

    @Override
    public EsCellAttemptDTO toDto(EsCellAttempt esCellAttempt) {
        EsCellAttemptDTO esCellAttemptDTO = null;
        if (esCellAttempt != null)
        {
            esCellAttemptDTO = entityMapper.toTarget(esCellAttempt, EsCellAttemptDTO.class);
            setEsCellName(esCellAttemptDTO, esCellAttempt);
        }
        return esCellAttemptDTO;
    }

    private void setEsCellName(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        TargRepEsCell targRepEsCell = targRepEsCellService.getTargRepEsCellById(esCellAttempt.getTargRepEsCellId());
        if (targRepEsCell != null)
        {
            esCellAttemptDTO.setEsCellName(targRepEsCell.getName());
        }
    }

    @Override
    public EsCellAttempt toEntity(EsCellAttemptDTO esCellAttemptDTO) {
        EsCellAttempt esCellAttempt = entityMapper.toTarget(esCellAttemptDTO, EsCellAttempt.class);
        setComment(esCellAttemptDTO, esCellAttempt);
        setTotalChimeras(esCellAttemptDTO, esCellAttempt);
        if (esCellAttemptDTO.getExperimental() == null) {
            esCellAttempt.setExperimental(false);
        }
        setEsCellId(esCellAttemptDTO, esCellAttempt);
        return esCellAttempt;
    }

    private void setEsCellId(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        TargRepEsCell targRepEsCell = targRepEsCellService.getTargRepEsCellByNameFailsIfNull(esCellAttemptDTO.getEsCellName());
        if (targRepEsCell != null)
        {
            esCellAttempt.setTargRepEsCellId(targRepEsCell.getId());
        }
    }

    private void setComment(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        if (Strings.isBlank(esCellAttemptDTO.getComment()))
        {
            esCellAttempt.setComment(null);
        }
        else
        {
            esCellAttempt.setComment(esCellAttemptDTO.getComment());
        }
    }

    private void setTotalChimeras(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        if (esCellAttemptDTO.getTotalFemaleChimeras() != null && esCellAttemptDTO.getTotalMaleChimeras() != null)
        {
            Integer femaleChimeras = esCellAttemptDTO.getTotalFemaleChimeras();
            Integer maleChimeras = esCellAttemptDTO.getTotalMaleChimeras();
            Integer total = femaleChimeras + maleChimeras;
            esCellAttempt.setTotalChimeras(total);
        }
    }

    public void setCassetteTransmission (EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(false);

        if (esCellAttemptDTO.getCassetteTransmissionVerified() == null)
        {
            LocalDateTime date = null;

            if (esCellAttempt.getPlan().getOutcomes() != null)
            {
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
                    if (date != null) {
                        esCellAttempt.setCassetteTransmissionVerified(date.toLocalDate());
                        esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(true);
                    }
                }
            }
        }
        else {
            esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(true);
        }
    }
}
