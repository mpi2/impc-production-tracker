package org.gentar.biology.plan.attempt.es_cell;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.colony.status_stamp.ColonyStatusStamp;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainService;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class EsCellAttemptMapper implements Mapper<EsCellAttempt, EsCellAttemptDTO>
{
    private final EntityMapper entityMapper;
    private final TargRepEsCellService targRepEsCellService;
    private final StrainService strainService;

    public EsCellAttemptMapper(EntityMapper entityMapper,
                               TargRepEsCellService targRepEsCellService,
                               StrainService strainService)
    {
        this.entityMapper = entityMapper;
        this.targRepEsCellService = targRepEsCellService;
        this.strainService = strainService;
    }

    @Override
    public EsCellAttemptDTO toDto(EsCellAttempt esCellAttempt)
    {
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
            esCellAttemptDTO.setTargRepEsCellName(targRepEsCell.getName());
        }
    }

    @Override
    public EsCellAttempt toEntity(EsCellAttemptDTO esCellAttemptDTO)
    {
        EsCellAttempt esCellAttempt = entityMapper.toTarget(esCellAttemptDTO, EsCellAttempt.class);
        setComment(esCellAttemptDTO, esCellAttempt);
        setTotalChimeras(esCellAttemptDTO, esCellAttempt);
        if (esCellAttempt.getExperimental() == null) {
            esCellAttempt.setExperimental(false);
        }
        setEsCellId(esCellAttemptDTO, esCellAttempt);
        setStrains(esCellAttemptDTO, esCellAttempt);
        return esCellAttempt;
    }

    private void setStrains(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        if (esCellAttemptDTO.getBlastStrainName() != null)
        {
            Strain blastStrain = strainService.getStrainByName(esCellAttemptDTO.getBlastStrainName());
            esCellAttempt.setBlastStrain(blastStrain);
        }

        if (esCellAttemptDTO.getTestCrossStrainName() != null)
        {
            Strain testCrossStrain = strainService.getStrainByName(esCellAttemptDTO.getTestCrossStrainName());
            esCellAttempt.setTestCrossStrain(testCrossStrain);
        }
    }

    private void setEsCellId(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        if (esCellAttempt.getTargRepEsCellId() == null)
        {
            TargRepEsCell targRepEsCell = targRepEsCellService.getTargRepEsCellByNameFailsIfNull(esCellAttemptDTO.getTargRepEsCellName());
            if (targRepEsCell != null) {
                esCellAttempt.setTargRepEsCellId(targRepEsCell.getId());
            }
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
            if (!Objects.equals(esCellAttempt.getTotalChimeras(), total)) {
                esCellAttempt.setTotalChimeras(total);
            }
        }
    }

    public void setCassetteTransmission(EsCellAttemptDTO esCellAttemptDTO, EsCellAttempt esCellAttempt)
    {
        if (esCellAttempt.getCassetteTransmissionVerifiedAutoComplete() == null)
        {
            esCellAttempt.setCassetteTransmissionVerifiedAutoComplete(false);
        }

        if (esCellAttemptDTO.getCassetteTransmissionVerified() == null)
        {
            LocalDateTime date;

            if (esCellAttempt.getPlan().getOutcomes() != null)
            {
                Outcome outcome = esCellAttempt.getPlan().getOutcomes().stream()
                        .filter(outcome1 -> "Genotype Confirmed".equals(outcome1.getColony().getProcessDataStatus().getName()))
                        .findFirst()
                        .orElse(null);
                assert outcome != null;
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
