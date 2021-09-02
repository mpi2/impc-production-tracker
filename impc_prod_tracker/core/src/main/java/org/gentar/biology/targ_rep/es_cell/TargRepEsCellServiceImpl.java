package org.gentar.biology.targ_rep.es_cell;

import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargRepEsCellServiceImpl implements TargRepEsCellService
{
    private final TargRepEsCellRepository targRepEsCellRepository;

    private static final String TARG_REP_ES_CELL_NOT_FOUND_ERROR = "Es cell does not exist.";
    private static final String TARG_REP_ES_CELL_NAME_ERROR = "Es cell name [%s] does not exist.";

    public TargRepEsCellServiceImpl(TargRepEsCellRepository targRepEsCellRepository)
    {
        this.targRepEsCellRepository = targRepEsCellRepository;
    }

    @Override
    public TargRepEsCell getTargRepEsCellById (Long id)
    {
        TargRepEsCell targRepEsCell = targRepEsCellRepository.findTargRepEsCellById(id);
        if (targRepEsCell == null)
        {
            throw new UserOperationFailedException(TARG_REP_ES_CELL_NOT_FOUND_ERROR);
        }
        return targRepEsCell;
    }

    @Override
    public TargRepEsCell getTargRepEsCellByNameFailsIfNull(String name)
    {
        TargRepEsCell targRepEsCell = targRepEsCellRepository.findTargRepEsCellByName(name);
        if (targRepEsCell == null)
        {
            throw new UserOperationFailedException(
                    String.format(TARG_REP_ES_CELL_NAME_ERROR, name));
        }
        return targRepEsCell;
    }

    @Override
    public List<TargRepEsCell> getTargRepEscellByAlleleFailsIfNull(TargRepAllele allele) throws UserOperationFailedException
    {
        List<TargRepEsCell> esCells = targRepEsCellRepository.findTargRepEsCellByAllele(allele);
        if (esCells.isEmpty())
        {
            throw new NotFoundException("There are not ES Cells available for [" + allele.getGene().getSymbol()
                                        + "] marker_symbol does not exist.");
        }
        return esCells;
    }
}
